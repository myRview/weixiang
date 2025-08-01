package com.hk.service.article.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.entity.article.CategoryEntity;
import com.hk.mapper.article.CategoryMapper;
import com.hk.param.CategorySearchParam;
import com.hk.service.article.CategoryService;
import com.hk.vo.article.CategoryVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 分类表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2025-07-29
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryEntity> implements CategoryService {
    @Override
    public boolean saveCategory(CategoryVO categoryVO) {
        CategoryEntity category = new CategoryEntity();
        category.setName(categoryVO.getName());
        category.setDescription(categoryVO.getDescription());
        return this.save(category);
    }

    @Override
    public boolean updateCategory(CategoryVO categoryVO) {
        CategoryEntity category = new CategoryEntity();
        category.setId(categoryVO.getId());
        category.setName(categoryVO.getName());
        category.setDescription(categoryVO.getDescription());
        return this.updateById(category);
    }

    @Override
    public List<CategoryVO> getCategoryList(String categoryName) {
        LambdaQueryWrapper<CategoryEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(categoryName), CategoryEntity::getName, categoryName);
        queryWrapper.select(CategoryEntity::getId, CategoryEntity::getName, CategoryEntity::getDescription);
        List<CategoryEntity> list = this.list(queryWrapper);
        return list.stream().map(CategoryVO::convertVo).collect(Collectors.toList());
    }

    @Override
    public IPage<CategoryVO> selectCategoryPage(CategorySearchParam param) {
        String categoryName = param.getCategoryName();

        LambdaQueryWrapper<CategoryEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(categoryName)) {
            queryWrapper.eq(CategoryEntity::getName, categoryName);
        }
        queryWrapper.select(CategoryEntity::getId, CategoryEntity::getName, CategoryEntity::getDescription, CategoryEntity::getCreateTime);
        Page<CategoryEntity> page = this.page(new Page<>(param.getPageNum(), param.getPageSize()), queryWrapper);
        return page.convert(CategoryVO::convertVo);
    }
}
