package com.hk.vo.article;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 文章表
 * </p>
 *
 * @author hk
 * @since 2025-07-29
 */
@Data
public class ArticleAuditVO implements Serializable {


    private static final long serialVersionUID = 2275725074862564672L;

    @Schema(description = "文章id")
    @NotNull
    private Long articleId;

    @Schema(description = "审核状态:1-审核通过，2-审核不通过")
    @NotNull
    private Integer auditStatus;

    @Schema(description = "审核原因")
    private String auditReason;
}
