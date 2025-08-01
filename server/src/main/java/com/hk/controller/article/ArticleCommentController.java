package com.hk.controller.article;

import com.hk.common.ResponseResult;
import com.hk.service.article.ArticleCommentService;
import com.hk.vo.article.ArticleCommentVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 文章评论表 前端控制器
 * </p>
 *
 * @author hk
 * @since 2025-07-29
 */
@Tag(name = "文章评论管理")
@RestController
@RequestMapping("/comment")
public class ArticleCommentController {

    @Autowired
    private ArticleCommentService articleCommentService;

    /**
     * 获取文章评论列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取文章评论列表")
    public ResponseResult<List<ArticleCommentVO>> getArticleCommentList(@RequestParam Long articleId) {
        return ResponseResult.success(articleCommentService.getArticleCommentList(articleId));
    }

    /**
     * 添加文章评论
     */
    @PostMapping("/add")
    @Operation(summary = "添加文章评论")
    public ResponseResult<?> addArticleComment(ArticleCommentVO articleCommentVO) {
        boolean save = articleCommentService.addArticleComment(articleCommentVO);
        return save ? ResponseResult.success("添加成功") : ResponseResult.fail("添加失败");
    }

    /**
     * 删除文章评论
     */
    @PostMapping("/delete")
    @Operation(summary = "删除文章评论")
    public ResponseResult<?> deleteArticleComment(Long id) {
        boolean remove = articleCommentService.deleteArticleComment(id);
        return remove ? ResponseResult.success("删除成功") : ResponseResult.fail("删除失败");
    }



}
