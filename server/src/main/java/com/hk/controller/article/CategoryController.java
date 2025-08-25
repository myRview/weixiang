package com.hk.controller.article;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hk.aop.log.annotation.OperatorLog;
import com.hk.common.ResponseResult;
import com.hk.param.CategorySearchParam;
import com.hk.service.article.CategoryService;
import com.hk.vo.article.CategoryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 分类表 前端控制器
 * </p>
 *
 * @author hk
 * @since 2025-07-29
 */
@Tag(name = "文章分类管理")
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 添加分类
     */
    @PostMapping("/add")
    @Operation(summary = "添加分类")
//    @OperatorLog(value = "文章分类管理", desc = "添加分类")
    @PreAuthorize("@ss.hasPermission('/category/add')")
    public ResponseResult saveCategory(@RequestBody CategoryVO categoryVO) {
        return categoryService.saveCategory(categoryVO) ? ResponseResult.success("添加成功") : ResponseResult.fail("添加失败");
    }

    /**
     * 删除分类
     */
    @GetMapping("/delete")
    @Operation(summary = "删除分类")
//    @OperatorLog(value = "文章分类管理", desc = "删除分类")
    @PreAuthorize("@ss.hasPermission('/category/delete')")
    public ResponseResult deleteCategory(@RequestParam Long id) {
        return categoryService.deleteCategoryById(id) ? ResponseResult.success("删除成功") : ResponseResult.fail("删除失败");
    }

    /**
     * 修改分类
     */
    @PostMapping("/update")
    @Operation(summary = "修改分类")
//    @OperatorLog(value = "文章分类管理", desc = "修改分类")
    @PreAuthorize("@ss.hasPermission('/category/update')")
    public ResponseResult updateCategory(@RequestBody CategoryVO categoryVO) {
        return categoryService.updateCategory(categoryVO) ? ResponseResult.success("修改成功") : ResponseResult.fail("修改失败");
    }

    /**
     * 获取分类列表
     */
    @PostMapping("/list")
    @Operation(summary = "获取分类列表")
//    @OperatorLog(value = "文章分类管理", desc = "获取分类列表")
//    @PreAuthorize("@ss.hasPermission('/category/list')")
    public ResponseResult<List<CategoryVO>> getCategoryList(String categoryName) {
        return ResponseResult.success(categoryService.getCategoryList(categoryName));
    }

    /**
     * 获取分类分页列表
     */
    @PostMapping("/page")
    @Operation(summary = "获取分类分页列表")
//    @OperatorLog(value = "文章分类管理", desc = "获取分类分页列表")
    @PreAuthorize("@ss.hasPermission('/category/page')")
    public ResponseResult<IPage<CategoryVO>> selectCategoryPage(@RequestBody CategorySearchParam param) {
        return ResponseResult.success(categoryService.selectCategoryPage(param));
    }

}
