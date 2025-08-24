package com.hk.controller.article;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hk.aop.log.annotation.OperatorLog;
import com.hk.common.ResponseResult;
import com.hk.enums.ArticleAuditStatus;
import com.hk.enums.ArticlePublishStatus;
import com.hk.param.ArticleSearchParam;
import com.hk.service.article.ArticleService;
import com.hk.vo.article.ArticleAuditVO;
import com.hk.vo.article.ArticleEditVO;
import com.hk.vo.article.ArticleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 文章表 前端控制器
 * </p>
 *
 * @author hk
 * @since 2025-07-29
 */
@Tag(name = "文章管理")
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 添加文章
     */
    @PostMapping("/add")
    @Operation(summary = "保存文章")
//    @OperatorLog(value = "文章管理", desc = "保存文章")
//    @PreAuthorize("@ss.hasPermission('/article/add')")
    public ResponseResult saveArticle(@RequestBody @Validated ArticleEditVO articleEditVO) {
        return articleService.saveArticle(articleEditVO) ? ResponseResult.success("添加成功") : ResponseResult.fail("添加失败");
    }

    /**
     * 删除文章
     */
    @GetMapping("/delete")
    @Operation(summary = "删除文章(作者)")
//    @OperatorLog(value = "文章管理", desc = "删除文章")
//    @PreAuthorize("@ss.hasPermission('/article/delete')")
    public ResponseResult deleteArticle(@RequestParam Long id) {
        return articleService.deleteArticle(id) ? ResponseResult.success("删除成功") : ResponseResult.fail("删除失败");
    }


    /**
     * 发布文章
     */
    @PostMapping("/publish")
    @Operation(summary = "发布文章")
//    @OperatorLog(value = "文章管理", desc = "发布文章")
//    @PreAuthorize("@ss.hasPermission('/article/publish')")
    public ResponseResult publishArticle(Long id) {
        return articleService.publishArticle(id) ? ResponseResult.success("修改成功") : ResponseResult.fail("修改失败");
    }

    /**
     * 获取当前作者的文章分页列表
     */
    @PostMapping("/author/page")
    @Operation(summary = "获取当前作者的文章分页列表(作者)")
//    @PreAuthorize("@ss.hasPermission('/article/author/page')")
    public ResponseResult<IPage<ArticleVO>> selectArticlePageByAuthor(@RequestBody @Validated ArticleSearchParam param) {
        return ResponseResult.success(articleService.selectArticlePageByAuthor(param));
//        return ResponseResult.success(articleService.selectArticlePageFromEs(param));
    }


    /**
     * 获取审批通过的文章分页列表
     */
    @PostMapping("/pass/page")
    @Operation(summary = "获取审批通过的文章分页列表")
//    @PreAuthorize("@ss.hasPermission('/article/pass/page')")
    public ResponseResult<IPage<ArticleVO>> selectPassArticlePage(@RequestBody ArticleSearchParam param) {
//        return ResponseResult.success(articleService.selectPassArticlePage(param));
        if (param.getPageSize() > 100) {
            param.setPageSize(100);
        }
        param.setAuditStatus(ArticleAuditStatus.PASS.getCode());
        param.setPublishStatus(ArticlePublishStatus.PUBLISHED.getCode());
        return ResponseResult.success(articleService.selectArticlePageFromEs(param));
    }

    /**
     * 获取文章详情
     */
    @GetMapping("/detail")
    @Operation(summary = "获取文章详情")
//        @PreAuthorize("@ss.hasPermission('/article/detail')")
    public ResponseResult<ArticleVO> selectArticleDetail(@RequestParam Long id) {
        return ResponseResult.success(articleService.selectArticleDetail(id));
    }

    /**
     * 点赞
     */
    @PostMapping("/like")
    @Operation(summary = "点赞和取消点赞")
//    @PreAuthorize("@ss.hasPermission('/article/like')")
    public ResponseResult<?> likeArticle(Long articleId, Integer isLike) {
        return articleService.likeArticle(articleId, isLike) ? ResponseResult.success("操作成功") : ResponseResult.fail("操作失败");
    }

    /**
     * 点赞状态
     */
    @GetMapping("/like/status")
    @Operation(summary = "点赞状态")
//    @PreAuthorize("@ss.hasPermission('/article/like/status')")
    public ResponseResult<Boolean> articleLikeStatus(Long articleId) {
        return ResponseResult.success(articleService.articleLikeStatus(articleId));
    }

    /**
     * 点赞数量
     */
    @GetMapping("/like/count")
    @Operation(summary = "点赞数量")
//    @PreAuthorize("@ss.hasPermission('/article/like/count')")
    public ResponseResult<Integer> articleLikeCount(Long articleId) {
        return ResponseResult.success(articleService.articleLikeCount(articleId));
    }

    /**
     * 获取阅读数量
     *
     * @param articleId
     * @return
     */
    @GetMapping("/view/count")
    @Operation(summary = "阅读数量")
//    @PreAuthorize("@ss.hasPermission('/article/view/count')")
    public ResponseResult<Integer> articleViewCount(Long articleId) {
        return ResponseResult.success(articleService.articleViewCount(articleId));
    }

    /**
     * 添加阅读数量
     *
     * @param articleId
     * @return
     */
    @GetMapping("/view/add")
    @Operation(summary = "添加阅读数量")
//    @PreAuthorize("@ss.hasPermission('/article/view/add')")
    public ResponseResult<Boolean> addArticleViewCount(Long articleId) {
        return ResponseResult.success(articleService.addArticleViewCount(articleId));
    }

    /**
     * 获取已发布文章分页列表
     */
    @PostMapping("/page")
    @Operation(summary = "获取已发布文章分页列表(管理员)")
//    @PreAuthorize("@ss.hasPermission('/article/page')")
    public ResponseResult<IPage<ArticleVO>> selectArticlePage(@RequestBody ArticleSearchParam param) {
        param.setPublishStatus(ArticlePublishStatus.PUBLISHED.getCode());
        param.setAuditStatus(ArticleAuditStatus.PENDING.getCode());
        return ResponseResult.success(articleService.selectArticlePage(param));
//        return ResponseResult.success(articleService.selectArticlePageFromEs(param));
    }

    /**
     * 审核文章
     */
    @PostMapping("/audit")
    @Operation(summary = "审核文章(管理员)")
    @OperatorLog(value = "文章管理", desc = "审核文章")
    @PreAuthorize("@ss.hasPermission('/article/audit')")
    public ResponseResult<?> auditArticle(@RequestBody ArticleAuditVO auditVO) {
        return articleService.auditArticle(auditVO) ? ResponseResult.success("审核成功") : ResponseResult.fail("审核失败");
    }


}
