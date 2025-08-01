package com.hk.vo.article;

import com.alipay.api.domain.Article;
import com.hk.entity.article.ArticleEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 文章表
 * </p>
 *
 * @author hk
 * @since 2025-07-29
 */
@Data
public class ArticleVO implements Serializable {

    private static final long serialVersionUID = -4372481825355206039L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "状态，0-草稿，1-已发布")
    private Integer publishStatus;

    @Schema(description = "审核状态，0-待审核，1-审核通过，2-审核不通过")
    private Integer auditStatus;

    @Schema(description = "审核原因")
    private String auditReason;

    @Schema(description = "浏览量")
    private Integer viewCount;

    @Schema(description = "点赞量")
    private Integer likeCount;

    @Schema(description = "分类id")
    private Long categoryId;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "标签id")
    private List<Long> tagIds;

    public static ArticleVO convert(ArticleEntity article) {
        if (article == null) {
            return null;
        }
        ArticleVO articleVO = new ArticleVO();
        articleVO.setId(article.getId());
        articleVO.setTitle(article.getTitle());
        articleVO.setContent(article.getContent());
        articleVO.setUserId(article.getUserId());
        articleVO.setPublishStatus(article.getPublishStatus());
        articleVO.setAuditStatus(article.getAuditStatus());
        articleVO.setAuditReason(article.getAuditReason());
        articleVO.setViewCount(article.getViewCount());
        articleVO.setLikeCount(article.getLikeCount());
        articleVO.setCategoryId(article.getCategoryId());
        articleVO.setCreateTime(article.getCreateTime());
        return articleVO;
    }
}
