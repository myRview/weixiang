package com.hk.param;

import com.hk.common.PageBaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author huangkun
 * @date 2025/7/30 9:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleSearchParam extends PageBaseVO {

    @Schema(description = "标题")
    private String title;
    @Schema(description = "状态，0-草稿，1-已发布")
    private Integer publishStatus;
    @Schema(description = "审核状态，0-待审核，1-审核通过，2-审核不通过")
    private Integer auditStatus;
    @Schema(description = "分类id")
    private Long categoryId;
    @Schema(description = "标签Id集合")
    private List<Long> tagIds;
    @Schema(description = "作者Id")
    @NotNull
    private Long userId;
}
