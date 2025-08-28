package com.hk.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 区县表
 * </p>
 *
 * @author hk
 * @since 2025-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_county")
@Schema(name = "CountyEntity对象", description = "区县表")
public class CountyEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description ="区县名称")
    private String countyName;

    @Schema(description ="区县编码(行政区划代码)")
    private String countyCode;

    @Schema(description ="所属城市ID")
    private Long cityId;

    @Schema(description ="创建时间")
    private Date createTime;

    @Schema(description ="更新时间")
    private Date updateTime;
}
