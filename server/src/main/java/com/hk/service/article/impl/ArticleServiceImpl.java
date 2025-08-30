package com.hk.service.article.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.cache.RedisService;
import com.hk.common.ErrorCode;
import com.hk.constants.BaseConstant;
import com.hk.context.UserContext;
import com.hk.elasticsearch.vo.ArticleEsVO;
import com.hk.entity.article.ArticleEntity;
import com.hk.entity.article.ArticleTagEntity;
import com.hk.enums.ArticleAuditStatus;
import com.hk.enums.ArticlePublishStatus;
import com.hk.enums.PushTypeEnum;
import com.hk.exception.BusinessException;
import com.hk.mapper.article.ArticleMapper;
import com.hk.param.ArticleSearchParam;
import com.hk.scoket.PushMessageBaseVO;
import com.hk.scoket.WebSocketPushService;
import com.hk.service.article.ArticleService;
import com.hk.service.article.ArticleTagService;
import com.hk.service.message.UserMessageService;
import com.hk.service.user.UserService;
import com.hk.vo.article.ArticleAuditVO;
import com.hk.vo.article.ArticleEditVO;
import com.hk.vo.article.ArticleTagVO;
import com.hk.vo.article.ArticleVO;
import com.hk.vo.message.UserMessageVO;
import com.hk.vo.user.UserCacheVo;
import com.hk.vo.user.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 文章表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2025-07-29
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, ArticleEntity> implements ArticleService {

    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserMessageService userMessageService;
    @Autowired
    private WebSocketPushService articleApprovalHandler;
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
    ;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveArticle(ArticleEditVO articleEditVO) {
        Long id = articleEditVO.getId();
        ArticleEntity newArticle = new ArticleEntity();
        newArticle.setTitle(articleEditVO.getTitle());
        newArticle.setContent(articleEditVO.getContent());
        newArticle.setCategoryId(articleEditVO.getCategoryId());
        newArticle.setPublishStatus(articleEditVO.getPublishStatus());
        newArticle.setAuditStatus(ArticleAuditStatus.PENDING.getCode());
        Long currentUserId = UserContext.getCurrentUserId();
        if (id != null && id > 0) {
            ArticleEntity article = this.getOne(new LambdaQueryWrapper<ArticleEntity>().eq(ArticleEntity::getId, id)
                    .select(ArticleEntity::getUserId, ArticleEntity::getId)
                    .last(" limit 1")
            );
            if (article == null) throw new BusinessException(ErrorCode.BAD_REQUEST, "文章不存在");
            if (!article.getUserId().equals(currentUserId))
                throw new BusinessException(ErrorCode.BAD_REQUEST, "没有权限修改文章");
            newArticle.setId(id);
        } else {
            newArticle.setUserId(currentUserId);
        }
        //管理员文章不用审批
        if (UserContext.isAdmin()) {
            newArticle.setAuditStatus(ArticleAuditStatus.PASS.getCode());
        }
        boolean save = this.saveOrUpdate(newArticle);
        if (save) {
            articleTagService.deleteByArticleId(newArticle.getId());
            List<Long> tagIds = articleEditVO.getTagIds();
            List<ArticleTagEntity> tagEntityList = tagIds.stream().map(tagId -> {
                ArticleTagEntity tagEntity = new ArticleTagEntity();
                tagEntity.setArticleId(newArticle.getId());
                tagEntity.setTagId(tagId);
                return tagEntity;
            }).collect(Collectors.toList());
            articleTagService.saveBatch(tagEntityList);
        }
        return save;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteArticle(Long id) {
        checkPermission(id);
        articleTagService.deleteByArticleId(id);
        boolean exists = elasticsearchOperations.exists(String.valueOf(id), ArticleEsVO.class);
        if (exists) {
            elasticsearchOperations.delete(String.valueOf(id), ArticleEsVO.class);
        }
        return this.removeById(id);
    }

    @Override
    public Boolean publishArticle(Long id) {
        checkPermission(id);
        ArticleEntity entity = new ArticleEntity();
        entity.setId(id);
        entity.setPublishStatus(ArticlePublishStatus.PUBLISHED.getCode());
        return this.updateById(entity);
    }

    @Override
    public IPage<ArticleVO> selectArticlePageByAuthor(ArticleSearchParam param) {
        Map<Long, List<ArticleTagVO>> articleTagMap = new HashMap<>();
        LambdaQueryWrapper<ArticleEntity> queryWrapper = getQueryWrapper(articleTagMap, param);
        queryWrapper.eq(ArticleEntity::getUserId, param.getUserId());
        queryWrapper.select(ArticleEntity::getId, ArticleEntity::getTitle, ArticleEntity::getAuditStatus,
                ArticleEntity::getAuditReason, ArticleEntity::getPublishStatus,
                ArticleEntity::getCategoryId, ArticleEntity::getCreateTime);
        Page<ArticleEntity> page = this.page(new Page<>(param.getPageNum(), param.getPageSize()), queryWrapper);
        return buildPage(page, articleTagMap);
    }

    @Override
    public IPage<ArticleVO> selectPassArticlePage(ArticleSearchParam param) {
        Map<Long, List<ArticleTagVO>> articleTagMap = new HashMap<>();
        param.setAuditStatus(ArticleAuditStatus.PASS.getCode());
        param.setPublishStatus(ArticlePublishStatus.PUBLISHED.getCode());
        LambdaQueryWrapper<ArticleEntity> queryWrapper = getQueryWrapper(articleTagMap, param);
        queryWrapper.select(ArticleEntity::getId, ArticleEntity::getTitle, ArticleEntity::getContent,
                ArticleEntity::getViewCount, ArticleEntity::getLikeCount, ArticleEntity::getUserId,
                ArticleEntity::getCategoryId, ArticleEntity::getCreateTime);
        Page<ArticleEntity> page = this.page(new Page<>(param.getPageNum(), param.getPageSize()), queryWrapper);
        return buildPage(page, articleTagMap);
    }

    @Override
    public ArticleVO selectArticleDetail(Long id) {
        if (id == null) throw new BusinessException(ErrorCode.BAD_REQUEST, "文章id不能为空");
        ArticleVO articleVO = null;
        articleVO = (ArticleVO) redisService.getHash(getArticleDetailKey(), String.valueOf(id));
        if (articleVO != null) {
            return articleVO;
        }
        ArticleEsVO articleEsVO = elasticsearchOperations.get(String.valueOf(id), ArticleEsVO.class);
        if (articleEsVO != null) {
            articleVO = ArticleEsVO.convertToVO(articleEsVO);
        } else {
            ArticleEntity article = this.getById(id);
            if (article == null) throw new BusinessException(ErrorCode.BAD_REQUEST, "文章不存在");
            articleVO = ArticleVO.convert(article);
            List<ArticleTagVO> tagVOList = articleTagService.selectListByArticleId(id);
            if (CollectionUtil.isNotEmpty(tagVOList)) {
                articleVO.setTagIds(tagVOList.stream().map(ArticleTagVO::getTagId).collect(Collectors.toList()));
            }
            UserVO userVO = userService.selectById(article.getUserId());
            if (userVO != null) {
                articleVO.setUserName(userVO.getUserName());
                articleVO.setUserAvatar(userVO.getAvatar());
            }
        }
        redisService.putHash(getArticleDetailKey(), String.valueOf(id), articleVO);
        redisService.expire(getArticleDetailKey(), RandomUtil.randomInt(1, 5), TimeUnit.HOURS);
        return articleVO;
    }

    @Override
    public Boolean likeArticle(Long articleId, Integer isLike) {
        Long currentUserId = UserContext.getCurrentUserId();
        if (currentUserId == null) throw new BusinessException(ErrorCode.NOT_LOGIN, "用户未登录");
        String value = String.valueOf(currentUserId);
        if (isLike != null && isLike == 1) {
            redisService.addSet(getLikeKey(articleId), value);
        } else {
            redisService.removeSet(getLikeKey(articleId), value);
        }
        return true;
    }

    @Override
    public Boolean articleLikeStatus(Long articleId) {
        Long currentUserId = UserContext.getCurrentUserId();
        if (currentUserId == null) return false;
        String value = String.valueOf(currentUserId);
        return redisService.isMemberSet(getLikeKey(articleId), value);
    }

    @Override
    public Integer articleLikeCount(Long articleId) {
        Set<String> membersSet = redisService.membersSet(getLikeKey(articleId));
        return CollectionUtil.isEmpty(membersSet) ? 0 : membersSet.size();
    }

    @Override
    public Integer articleViewCount(Long articleId) {
        Set<String> membersSet = redisService.membersSet(getViewKey(articleId));
        return CollectionUtil.isEmpty(membersSet) ? 0 : membersSet.size();
    }

    @Override
    public Boolean addArticleViewCount(Long articleId) {
        Long currentUserId = UserContext.getCurrentUserId();
        if (currentUserId == null) return false;
        String value = String.valueOf(currentUserId);
        Boolean isRead = redisService.isMemberSet(getViewKey(articleId), value);
        if (isRead) return false;
        redisService.addSet(getViewKey(articleId), value);
        return true;
    }

    @Override
    public IPage<ArticleVO> selectArticlePage(ArticleSearchParam param) {
        Map<Long, List<ArticleTagVO>> articleTagMap = new HashMap<>();
        param.setPublishStatus(ArticlePublishStatus.PUBLISHED.getCode());

        LambdaQueryWrapper<ArticleEntity> queryWrapper = getQueryWrapper(articleTagMap, param);

        queryWrapper.select(ArticleEntity::getId, ArticleEntity::getTitle, ArticleEntity::getAuditStatus,
                ArticleEntity::getUserId, ArticleEntity::getCategoryId, ArticleEntity::getCreateTime);
        Page<ArticleEntity> page = this.page(new Page<>(param.getPageNum(), param.getPageSize()), queryWrapper);
        return buildPage(page, articleTagMap);
    }

    /**
     * 从ES中获取数据
     *
     * @param param
     * @return
     */
    @Override
    public IPage<ArticleVO> selectArticlePageFromEs(ArticleSearchParam param) {
        String searchText = param.getSearchText();
        Integer publishStatus = param.getPublishStatus();
        Integer auditStatus = param.getAuditStatus();
        Long categoryId = param.getCategoryId();
        List<Long> tagIds = param.getTagIds();
        Long userId = param.getUserId();
        Integer pageNum = param.getPageNum();
        Integer pageSize = param.getPageSize();
        Criteria criteria = new Criteria();
        if (StringUtils.isNotBlank(searchText)) {
            criteria.and(new Criteria("title").or("content").contains(searchText));
        }
        // 精确匹配条件
        if (publishStatus != null) {
            criteria.and(new Criteria("publishStatus").is(publishStatus));
        }
        if (auditStatus != null) {
            criteria.and(new Criteria("auditStatus").is(auditStatus));
        }
        if (userId != null) {
            criteria.and(new Criteria("userId").is(userId));
        }
        if (categoryId != null) {
            criteria.and(new Criteria("categoryId").is(categoryId));
        }
        // 多值匹配（相当于旧版的 termsQuery）
        if (CollectionUtil.isNotEmpty(tagIds)) {
            criteria.and(new Criteria("tagIds").in(tagIds));
        }
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "createTime"));
        CriteriaQuery query = new CriteriaQuery(criteria, pageable);
        SearchHits<ArticleEsVO> searchHits = elasticsearchOperations.search(query, ArticleEsVO.class);
        IPage<ArticleVO> page = new Page<>(pageNum, pageSize);
        List<ArticleVO> articleVOS = new ArrayList<>();
        if (searchHits.hasSearchHits()) {
            searchHits.getSearchHits().forEach(searchHit -> {
                ArticleEsVO articleEsVO = searchHit.getContent();
                articleVOS.add(ArticleEsVO.convertToVO(articleEsVO));
            });
        }
        page.setRecords(articleVOS);
        page.setTotal(searchHits.getTotalHits());
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean auditArticle(ArticleAuditVO auditVO) {
        ArticleEntity article = this.getById(auditVO.getArticleId());
        if (article == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "文章不存在");
        }
        Integer status = auditVO.getAuditStatus();
        ArticleAuditStatus auditStatus = ArticleAuditStatus.getByCode(status);
        if (auditStatus == null) throw new BusinessException(ErrorCode.BAD_REQUEST, "审核状态不正确");

        article.setAuditStatus(status);
        if (status == ArticleAuditStatus.PASS.getCode()) {
            auditVO.setAuditReason("");
        }
        article.setAuditReason(auditVO.getAuditReason());


        boolean update = this.updateById(article);
        if (update) {
            String message = status == ArticleAuditStatus.PASS.getCode() ?
                    "您的文章《" + article.getTitle() + "》已通过审核"
                    : "您的文章《" + article.getTitle() + "》未通过审核，原因：" + auditVO.getAuditReason();
            UserMessageVO messageVO = new UserMessageVO();
            messageVO.setUserId(article.getUserId());
            messageVO.setMessage(message);
            userMessageService.saveMessage(messageVO);
            PushMessageBaseVO<UserMessageVO> messageBaseVO = new PushMessageBaseVO(PushTypeEnum.ARTICLE.getCode(), messageVO);
            articleApprovalHandler.sendMessageToUser(article.getUserId().toString(), messageBaseVO);
        }
        return update;
    }

    @Override
    public List<ArticleVO> selectAll(Integer offsetMinute) {
        LambdaQueryWrapper<ArticleEntity> queryWrapper = new LambdaQueryWrapper<>(ArticleEntity.class);
        if (offsetMinute != null) {
            //文章的修改在当前时间减去offsetMinute的时间
            Date recentDate = DateUtil.offsetMinute(DateUtil.date(), -offsetMinute);
            queryWrapper.ge(ArticleEntity::getUpdateTime, recentDate);
        }
        List<ArticleEntity> articleEntities = this.list(queryWrapper);
        if (CollectionUtil.isEmpty(articleEntities)) return null;

        Map<Long, UserVO> userVOMap = selectUser(articleEntities);

        List<Long> articleIds = articleEntities.stream().map(ArticleEntity::getId).collect(Collectors.toList());
        Map<Long, List<Long>> articleTagMap = articleTagService.selectMapByArticleId(articleIds);
        return articleEntities.stream().map(articleEntity -> {
                    ArticleVO articleVO = new ArticleVO();
                    BeanUtil.copyProperties(articleEntity, articleVO);
                    List<Long> tagIds = articleTagMap.get(articleEntity.getId());
                    if (CollectionUtil.isNotEmpty(tagIds)) {
                        articleVO.setTagIds(tagIds);
                    }
                    UserVO userVO = userVOMap.get(articleEntity.getUserId());
                    if (userVO != null) {
                        articleVO.setUserName(userVO.getUserName());
                        articleVO.setUserAvatar(userVO.getAvatar());
                    }
                    return articleVO;
                }
        ).collect(Collectors.toList());
    }

    private String getArticleDetailKey() {
        return String.format("%s:%s", BaseConstant.CACHE_PREFIX, "article:detail");
    }


    private String getLikeKey(Long articleId) {
        return String.format("%s:%s:%s", BaseConstant.CACHE_PREFIX, "article:like", articleId);
    }

    private String getViewKey(Long articleId) {
        return String.format("%s:%s:%s", BaseConstant.CACHE_PREFIX, "article:view", articleId);
    }

    private IPage<ArticleVO> buildPage(Page<ArticleEntity> page, Map<Long, List<ArticleTagVO>> articleTagMap) {
        Map<Long, UserVO> userVOMap = selectUser(page.getRecords());
        return page.convert(articleEntity -> {
            ArticleVO articleVO = new ArticleVO();
            BeanUtil.copyProperties(articleEntity, articleVO);
            List<ArticleTagVO> tagVOList = articleTagMap.get(articleEntity.getId());
            if (CollectionUtil.isNotEmpty(tagVOList)) {
                articleVO.setTagIds(tagVOList.stream().map(ArticleTagVO::getTagId).collect(Collectors.toList()));
            }
            UserVO userVO = userVOMap.get(articleEntity.getUserId());
            if (userVO != null) {
                articleVO.setUserName(userVO.getUserName());
                articleVO.setUserAvatar(userVO.getAvatar());
            }
            return articleVO;
        });
    }

    private Map<Long, UserVO> selectUser(List<ArticleEntity> records) {
        if (CollectionUtil.isEmpty(records)) {
            return new HashMap<>();
        }
        List<Long> userIds = records.stream().map(ArticleEntity::getUserId).distinct().collect(Collectors.toList());
        List<UserVO> userVOList = userService.selectByIds(userIds);
        return userVOList.stream().collect(Collectors.toMap(UserVO::getId, user -> user));
    }

    private LambdaQueryWrapper<ArticleEntity> getQueryWrapper(Map<Long, List<ArticleTagVO>> articleTagMap, ArticleSearchParam param) {
        List<ArticleTagVO> articleTagVOS = articleTagService.selectList(param.getTagIds());
        List<Long> articleIds = new ArrayList<>();
        articleIds.add(0L);
        if (CollectionUtil.isNotEmpty(articleTagVOS)) {
            articleIds = articleTagVOS.stream().map(ArticleTagVO::getArticleId).distinct().collect(Collectors.toList());
            Map<Long, List<ArticleTagVO>> map = articleTagVOS.stream().collect(Collectors.groupingBy(ArticleTagVO::getArticleId));
            articleTagMap.putAll(map);
        }
        LambdaQueryWrapper<ArticleEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(param.getTitle())) {
            queryWrapper.like(ArticleEntity::getTitle, param.getTitle());
        }
        if (param.getPublishStatus() != null) {
            queryWrapper.eq(ArticleEntity::getPublishStatus, param.getPublishStatus());
        }
        if (param.getAuditStatus() != null) {
            queryWrapper.eq(ArticleEntity::getAuditStatus, param.getAuditStatus());
        }
        if (param.getCategoryId() != null) {
            queryWrapper.eq(ArticleEntity::getCategoryId, param.getCategoryId());
        }
        queryWrapper.in(ArticleEntity::getId, articleIds);
        queryWrapper.orderByDesc(ArticleEntity::getId);
        return queryWrapper;
    }


    private void checkPermission(Long id) {
        ArticleEntity article = this.getById(id);
        if (article == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "文章不存在");
        }
        UserCacheVo currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "用户不存在");
        }
        if (!currentUser.getUserId().equals(article.getUserId())) {
            throw new BusinessException(ErrorCode.NO_PERMISSION, "没有权限");
        }
    }
}
