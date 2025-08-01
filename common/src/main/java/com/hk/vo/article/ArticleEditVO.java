package com.hk.vo.article;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author huangkun
 * @date 2025/7/30 9:24
 */
@Data
public class ArticleEditVO implements Serializable {

    private static final long serialVersionUID = -6177075468445923075L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "标题")
    @NotBlank(message = "标题不能为空")
    private String title;

    @Schema(description = "内容")
    @NotBlank(message = "内容不能为空")
    private String content;

    @Schema(description = "分类id")
    private Long categoryId;

    @Schema(description = "标签id列表")
    private List<Long> tagIds;

    @Schema(description = "状态，0-草稿，1-已发布")
    @NotNull(message = "状态不能为空")
    private Integer publishStatus;
}
