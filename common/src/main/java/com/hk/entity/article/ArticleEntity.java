package com.hk.entity.article;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 文章表
 * </p>
 *
 * @author hk
 * @since 2025-07-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("article")
@Schema(name = "ArticleEntity对象", description = "文章表")
public class ArticleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "用户id")
    private Long userId;

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

    @Schema(description = "删除标识,0-正常，1-已删除")
    private Integer deleteFlag;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;
}
