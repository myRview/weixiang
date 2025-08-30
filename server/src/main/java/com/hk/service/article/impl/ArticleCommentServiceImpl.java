package com.hk.service.article.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.cache.RedisService;
import com.hk.common.ErrorCode;
import com.hk.constants.BaseConstant;
import com.hk.context.UserContext;
import com.hk.entity.article.ArticleCommentEntity;
import com.hk.entity.article.ArticleEntity;
import com.hk.exception.BusinessException;
import com.hk.mapper.article.ArticleCommentMapper;
import com.hk.service.article.ArticleCommentService;
import com.hk.service.article.ArticleService;
import com.hk.service.user.UserService;
import com.hk.vo.article.ArticleCommentVO;
import com.hk.vo.article.ArticleVO;
import com.hk.vo.user.UserCacheVo;
import com.hk.vo.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 文章评论表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2025-07-29
 */
@Service
public class ArticleCommentServiceImpl extends ServiceImpl<ArticleCommentMapper, ArticleCommentEntity> implements ArticleCommentService {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @Override
    public List<ArticleCommentVO> getArticleCommentList(Long articleId) {
        if (articleId == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }
        Object hash = redisService.getHash(getHashKey(), String.valueOf(articleId));
        if (hash != null) return (List<ArticleCommentVO>) hash;

        LambdaQueryWrapper<ArticleCommentEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleCommentEntity::getArticleId, articleId);
        queryWrapper.select(ArticleCommentEntity::getId, ArticleCommentEntity::getContent,
                ArticleCommentEntity::getUserId, ArticleCommentEntity::getCreateTime);
        queryWrapper.orderByDesc(ArticleCommentEntity::getId);
        List<ArticleCommentEntity> commentEntityList = this.list(queryWrapper);
        if (CollectionUtil.isEmpty(commentEntityList)) return new ArrayList<>();

        List<Long> userIds = commentEntityList.stream().map(ArticleCommentEntity::getUserId).collect(Collectors.toList());
        List<UserVO> userVOS = userService.selectByIds(userIds);

        List<ArticleCommentVO> commentVOS = commentEntityList.parallelStream().map(
                commentEntity -> {
                    ArticleCommentVO commentVO = ArticleCommentVO.convert(commentEntity);
                    if (CollectionUtil.isNotEmpty(userVOS)) {
                        Optional<UserVO> optional = userVOS.stream().filter(userVO -> userVO.getId().equals(commentEntity.getUserId())).findFirst();
                        if (optional.isPresent()) {
                            UserVO userVO = optional.get();
                            commentVO.setUserName(userVO.getUserName());
                            commentVO.setUserAvatar(userVO.getAvatar());
                        }
                    }
                    return commentVO;
                }
        ).collect(Collectors.toList());
        redisService.putHash(getHashKey(), String.valueOf(articleId), commentVOS);
        redisService.expire(getHashKey(), RandomUtil.randomInt(1, 5), TimeUnit.HOURS);
        return commentVOS;
    }


    @Override
    public boolean addArticleComment(ArticleCommentVO articleCommentVO) {
        Long articleId = articleCommentVO.getArticleId();
        ArticleEntity article = articleService.getById(articleId);
        if (article == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "文章不存在");
        }
        Long currentUserId = UserContext.getCurrentUserId();
        if (currentUserId == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "用户未登录");
        }
        ArticleCommentEntity articleCommentEntity = new ArticleCommentEntity();
        articleCommentEntity.setUserId(currentUserId);
        articleCommentEntity.setArticleId(articleId);
        articleCommentEntity.setContent(articleCommentVO.getContent());
        return this.save(articleCommentEntity);
    }

    @Override
    public boolean deleteArticleComment(Long id) {
        ArticleCommentEntity comment = this.getById(id);
        if (comment == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "评论不存在");
        }
        Long userId = UserContext.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "用户未登录");
        }
        if (userId.equals(comment.getUserId())) {
            return this.removeById(id);
        }
        ArticleEntity article = articleService.getById(comment.getArticleId());
        if (article != null && article.getUserId().equals(userId)) {
            return this.removeById(id);
        }
        if (UserContext.isAdmin()) {
            return this.removeById(id);
        }
        return false;
    }


    private String getHashKey() {
        return String.format("%s:%s", BaseConstant.CACHE_PREFIX, "article:comments");
    }
}
