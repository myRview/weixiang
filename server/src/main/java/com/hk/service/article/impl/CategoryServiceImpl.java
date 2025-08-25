package com.hk.service.article.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.cache.RedisService;
import com.hk.constants.BaseConstant;
import com.hk.entity.article.CategoryEntity;
import com.hk.mapper.article.CategoryMapper;
import com.hk.param.CategorySearchParam;
import com.hk.service.article.CategoryService;
import com.hk.vo.article.CategoryVO;
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
 * 分类表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2025-07-29
 */
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryEntity> implements CategoryService {

    @Autowired
    private RedisService redisService;

    @Override
    public boolean saveCategory(CategoryVO categoryVO) {
        CategoryEntity category = new CategoryEntity();
        category.setName(categoryVO.getName());
        category.setDescription(categoryVO.getDescription());
        boolean save = this.save(category);
        if (save) {
            categoryVO.setId(category.getId());
            redisService.putHash(getCategoryKey(), String.valueOf(categoryVO.getId()), categoryVO);
        }
        return save;
    }

    @Override
    public boolean updateCategory(CategoryVO categoryVO) {
        CategoryEntity category = new CategoryEntity();
        category.setId(categoryVO.getId());
        category.setName(categoryVO.getName());
        category.setDescription(categoryVO.getDescription());
        boolean update = this.updateById(category);
        if (update) {
            redisService.putHash(getCategoryKey(), String.valueOf(categoryVO.getId()), categoryVO);
        }
        return update;
    }

    @Override
    public List<CategoryVO> getCategoryList(String categoryName) {
        Map<Object, Object> objectObjectMap = redisService.entriesHash(getCategoryKey());
        if (CollectionUtil.isNotEmpty(objectObjectMap)) {
            List<CategoryVO> categoryVOS = objectObjectMap.values().stream().map(CategoryVO.class::cast).collect(Collectors.toList());
            if (StringUtils.isNotBlank(categoryName)) {
                categoryVOS = categoryVOS.stream().filter(categoryVO -> categoryVO.getName().contains(categoryName)).collect(Collectors.toList());
            }
            return categoryVOS;
        }
        LambdaQueryWrapper<CategoryEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(CategoryEntity::getId, CategoryEntity::getName, CategoryEntity::getDescription);
        List<CategoryEntity> list = this.list(queryWrapper);
        List<CategoryVO> categoryVOS = buildCache(list);
        if (StringUtils.isNotBlank(categoryName)) {
            categoryVOS = categoryVOS.stream().filter(categoryVO -> categoryVO.getName().contains(categoryName)).collect(Collectors.toList());
        }
        return categoryVOS;
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

    @Override
    public List<CategoryVO> buildCache(List<CategoryEntity> categoryList) {
        if (CollectionUtil.isNotEmpty(categoryList)) {
            List<CategoryVO> categoryVOS = categoryList.stream().map(CategoryVO::convertVo).collect(Collectors.toList());
            Map<String, CategoryVO> categoryMap = new HashMap<>();
            categoryVOS.forEach(categoryVO -> categoryMap.put(String.valueOf(categoryVO.getId()), categoryVO));
            redisService.putAllHash(getCategoryKey(), categoryMap);
            redisService.expire(getCategoryKey(),1, TimeUnit.HOURS);
            log.info("分类数据加载到缓存中,数量:{}", categoryVOS.size());
            return categoryVOS;
        }
        return null;
    }

    @Override
    public boolean deleteCategoryById(Long id) {
        boolean remove = this.removeById(id);
        if (remove) {
            redisService.deleteHash(getCategoryKey(), String.valueOf(id));
        }
        return remove;
    }

    private String getCategoryKey() {
        return String.format("%s%s", BaseConstant.CACHE_PREFIX, "category");
    }
}
