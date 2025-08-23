package com.hk.service.article.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.cache.RedisService;
import com.hk.constants.BaseConstant;
import com.hk.entity.article.TagEntity;
import com.hk.mapper.article.TagMapper;
import com.hk.param.TagSearchParam;
import com.hk.service.article.TagService;
import com.hk.vo.article.TagVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2025-07-29
 */
@Service
@Slf4j
public class TagServiceImpl extends ServiceImpl<TagMapper, TagEntity> implements TagService {


    @Autowired
    private RedisService redisService;

    @Override
    public boolean saveTag(TagVO tagVO) {
        TagEntity tag = new TagEntity();
        tag.setName(tagVO.getName());
        boolean save = this.save(tag);
        if (save) {
            tagVO.setId(tag.getId());
            redisService.putHash(getTagKey(), String.valueOf(tagVO.getId()), tagVO);
        }
        return save;
    }

    @Override
    public boolean updateTag(TagVO tagVO) {
        TagEntity tag = new TagEntity();
        tag.setName(tagVO.getName());
        tag.setId(tagVO.getId());
        boolean update = this.updateById(tag);
        if (update) {
            redisService.putHash(getTagKey(), String.valueOf(tagVO.getId()), tagVO);
        }
        return update;
    }

    @Override
    public List<TagVO> getTagList(String tagName) {
        Map<Object, Object> map = redisService.entriesHash(getTagKey());
        if (CollectionUtil.isNotEmpty(map)) {
            List<TagVO> tagVOList = map.values().stream().map(TagVO.class::cast).collect(Collectors.toList());
            if (StringUtils.isNotBlank(tagName)) {
                tagVOList = tagVOList.stream().filter(tagVO -> tagVO.getName().contains(tagName)).collect(Collectors.toList());
            }
            return tagVOList;
        }
        LambdaQueryWrapper<TagEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(TagEntity::getId, TagEntity::getName);
        List<TagEntity> list = this.list(queryWrapper);
        List<TagVO> tagVOList = buildCache(list);
        if (StringUtils.isNotBlank(tagName)) {
            tagVOList = tagVOList.stream().filter(tagVO -> tagVO.getName().contains(tagName)).collect(Collectors.toList());
        }
        return tagVOList;
    }

    @Override
    public IPage<TagVO> selectTagPage(TagSearchParam param) {
        String tagName = param.getTagName();
        LambdaQueryWrapper<TagEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(tagName)) {
            queryWrapper.like(TagEntity::getName, tagName);
        }
        queryWrapper.select(TagEntity::getId, TagEntity::getName, TagEntity::getCreateTime);
        Page<TagEntity> page = this.page(new Page<>(param.getPageNum(), param.getPageSize()), queryWrapper);
        return page.convert(TagVO::convertVo);
    }

    @Override
    public List<TagVO> buildCache(List<TagEntity> tagList) {
        if (CollectionUtil.isNotEmpty(tagList)) {
            List<TagVO> tagVOList = tagList.stream().map(TagVO::convertVo).collect(Collectors.toList());
            Map<String, TagVO> tagVOMap = new HashMap<>();
            tagVOList.forEach(tagVO -> tagVOMap.put(String.valueOf(tagVO.getId()), tagVO));
            redisService.putAllHash(getTagKey(), tagVOMap);
            redisService.expire(getTagKey(),1, TimeUnit.HOURS);
            log.info("标签数据加载到缓存中,数量:{}", tagVOList.size());
            return tagVOList;
        }
        return null;
    }

    private String getTagKey() {
        return String.format("%s%s", BaseConstant.CACHE_PREFIX, "tag");
    }
}
