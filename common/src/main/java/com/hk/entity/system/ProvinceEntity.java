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
 * 省份表
 * </p>
 *
 * @author hk
 * @since 2025-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_province")
@Schema(name = "ProvinceEntity对象", description = "省份表")
public class ProvinceEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description ="省份名称")
    private String provinceName;

    @Schema(description ="省份编码(行政区划代码)")
    private String provinceCode;

    @Schema(description ="省份邮编")
    private String postCode;

    @Schema(description ="创建时间")
    private Date createTime;

    @Schema(description ="更新时间")
    private Date updateTime;
}
