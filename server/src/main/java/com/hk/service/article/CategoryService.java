package com.hk.service.article;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.entity.article.CategoryEntity;
import com.hk.param.CategorySearchParam;
import com.hk.vo.article.CategoryVO;

import java.util.List;

/**
 * <p>
 * 分类表 服务类
 * </p>
 *
 * @author hk
 * @since 2025-07-29
 */
public interface CategoryService extends IService<CategoryEntity> {

    boolean saveCategory(CategoryVO categoryVO);

    boolean updateCategory(CategoryVO categoryVO);

    List<CategoryVO> getCategoryList(String categoryName);

    IPage<CategoryVO> selectCategoryPage(CategorySearchParam param);

    List<CategoryVO> buildCache(List<CategoryEntity> categoryList);
}
