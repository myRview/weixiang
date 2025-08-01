package com.hk.service.article.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.entity.article.ArticleTagEntity;
import com.hk.mapper.article.ArticleTagMapper;
import com.hk.service.article.ArticleTagService;
import com.hk.vo.article.ArticleTagVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 文章标签表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2025-07-29
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTagEntity> implements ArticleTagService {
    @Override
    public boolean deleteByArticleId(Long articleId) {
        LambdaQueryWrapper<ArticleTagEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTagEntity::getArticleId, articleId);
        return this.remove(queryWrapper);
    }

    @Override
    public List<ArticleTagVO> selectList(List<Long> tagIds) {
        LambdaQueryWrapper<ArticleTagEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (CollectionUtil.isNotEmpty(tagIds)) {
            queryWrapper.in(ArticleTagEntity::getTagId, tagIds);
        }
        queryWrapper.select(ArticleTagEntity::getTagId, ArticleTagEntity::getArticleId);
        List<ArticleTagEntity> list = this.list(queryWrapper);
        return list.stream().map(ArticleTagVO::convert).collect(Collectors.toList());
    }

    @Override
    public List<ArticleTagVO> selectListByArticleId(Long articleId) {
        LambdaQueryWrapper<ArticleTagEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTagEntity::getArticleId, articleId);
        queryWrapper.select(ArticleTagEntity::getTagId, ArticleTagEntity::getArticleId);
        List<ArticleTagEntity> list = this.list(queryWrapper);
        return list.stream().map(ArticleTagVO::convert).collect(Collectors.toList());
    }
}
