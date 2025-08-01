package com.hk.vo.article;

import com.hk.entity.article.TagEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 标签表
 * </p>
 *
 * @author hk
 * @since 2025-07-29
 */
@Data
public class TagVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "标签名称")
    private String name;

    @Schema(description = "创建时间")
    private Date createTime;

    public static TagVO convertVo(TagEntity tagEntity) {
        if (tagEntity == null) {
            return null;
        }
        TagVO tagVO = new TagVO();
        tagVO.setId(tagEntity.getId());
        tagVO.setName(tagEntity.getName());
        tagVO.setCreateTime(tagEntity.getCreateTime());
        return tagVO;
    }
}
