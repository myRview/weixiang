package com.hk.vo.article;

import com.hk.entity.article.CategoryEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 分类表
 * </p>
 *
 * @author hk
 * @since 2025-07-29
 */
@Data
public class CategoryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "父分类Id")
    private Long parentId;

    @Schema(description = "分类名称")
    private String name;

    @Schema(description = "分类描述")
    private String description;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    public static CategoryVO convertVo(CategoryEntity category) {
        if (category == null) {
            return null;
        }
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setId(category.getId());
        categoryVO.setParentId(category.getParentId());
        categoryVO.setName(category.getName());
        categoryVO.setDescription(category.getDescription());
        categoryVO.setCreateTime(category.getCreateTime());
        return categoryVO;
    }
}
