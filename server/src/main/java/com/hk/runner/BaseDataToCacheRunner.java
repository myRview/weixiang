package com.hk.runner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hk.entity.article.CategoryEntity;
import com.hk.entity.article.TagEntity;
import com.hk.service.article.CategoryService;
import com.hk.service.article.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author huangkun
 * @description: 基础分类、标签数据放在缓存中
 * @date 2025/8/23 16:08
 */
//@Component
@Slf4j
public class BaseDataToCacheRunner implements CommandLineRunner {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Override
    public void run(String... args) throws Exception {
        List<CategoryEntity> categoryList = categoryService.list(new LambdaQueryWrapper<>(CategoryEntity.class)
                .select(CategoryEntity::getId, CategoryEntity::getName));
        categoryService.buildCache(categoryList);

        List<TagEntity> tagList = tagService.list(new LambdaQueryWrapper<>(TagEntity.class)
                .select(TagEntity::getId, TagEntity::getName));
        tagService.buildCache(tagList);
    }
}
