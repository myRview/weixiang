package com.hk.service.article.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.entity.article.TagEntity;
import com.hk.mapper.article.TagMapper;
import com.hk.param.TagSearchParam;
import com.hk.service.article.TagService;
import com.hk.vo.article.TagVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
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
public class TagServiceImpl extends ServiceImpl<TagMapper, TagEntity> implements TagService {
    @Override
    public boolean saveTag(TagVO tagVO) {
        TagEntity tag = new TagEntity();
        tag.setName(tagVO.getName());
        return this.save(tag);
    }

    @Override
    public boolean updateTag(TagVO tagVO) {
        TagEntity tag = new TagEntity();
        tag.setName(tagVO.getName());
        tag.setId(tagVO.getId());
        return this.updateById(tag);
    }

    @Override
    public List<TagVO> getTagList(String tagName) {
        LambdaQueryWrapper<TagEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(tagName)) {
            queryWrapper.like(TagEntity::getName, tagName);
        }
        queryWrapper.select(TagEntity::getId, TagEntity::getName);
        return this.list(queryWrapper).stream().map(TagVO::convertVo).collect(Collectors.toList());
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
}
