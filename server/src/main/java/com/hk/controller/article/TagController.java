package com.hk.controller.article;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hk.aop.log.annotation.OperatorLog;
import com.hk.common.ResponseResult;
import com.hk.param.CategorySearchParam;
import com.hk.param.TagSearchParam;
import com.hk.service.article.TagService;
import com.hk.vo.article.TagVO;
import com.hk.vo.article.TagVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 标签表 前端控制器
 * </p>
 *
 * @author hk
 * @since 2025-07-29
 */
@Tag(name = "文章标签管理")
@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 添加标签
     */
    @PostMapping("/add")
    @Operation(summary = "添加标签")
//    @OperatorLog(value = "文章标签管理", desc = "添加标签")
    @PreAuthorize("@ss.hasPermission('/tag/add')")
    public ResponseResult saveTag(@RequestBody TagVO tagVO) {
        return tagService.saveTag(tagVO) ? ResponseResult.success("添加成功") : ResponseResult.fail("添加失败");
    }

    /**
     * 删除标签
     */
    @GetMapping("/delete")
    @Operation(summary = "删除标签")
//    @OperatorLog(value = "文章标签管理", desc = "删除标签")
    @PreAuthorize("@ss.hasPermission('/tag/delete')")
    public ResponseResult deleteTag(@RequestParam Long id) {
        return tagService.deleteTagById(id) ? ResponseResult.success("删除成功") : ResponseResult.fail("删除失败");
    }

    /**
     * 修改标签
     */
    @PostMapping("/update")
    @Operation(summary = "修改标签")
//    @OperatorLog(value = "文章标签管理", desc = "修改标签")
    @PreAuthorize("@ss.hasPermission('/tag/update')")
    public ResponseResult updateTag(@RequestBody TagVO tagVO) {
        return tagService.updateTag(tagVO) ? ResponseResult.success("修改成功") : ResponseResult.fail("修改失败");
    }

    /**
     * 获取标签列表
     */
    @PostMapping("/list")
    @Operation(summary = "获取标签列表")
//    @OperatorLog(value = "文章标签管理", desc = "获取标签列表")
//    @PreAuthorize("@ss.hasPermission('/tag/list')")
    public ResponseResult<List<TagVO>> getTagList(String tagName) {
        return ResponseResult.success(tagService.getTagList(tagName));
    }

    /**
     * 获取标签分页列表
     */
    @PostMapping("/page")
    @Operation(summary = "获取标签分页列表")
//    @OperatorLog(value = "文章标签管理", desc = "获取标签分页列表")
    @PreAuthorize("@ss.hasPermission('/tag/page')")
    public ResponseResult<IPage<TagVO>> selectTagPage(@RequestBody TagSearchParam param) {
        return ResponseResult.success(tagService.selectTagPage(param));
    }

}
