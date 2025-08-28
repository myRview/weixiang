package com.hk.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 城市表
 * </p>
 *
 * @author hk
 * @since 2025-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_city")
@Schema(name = "CityEntity对象", description = "城市表")
public class CityDataEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description ="城市名称")
    private String cityName;

    @Schema(description ="城市编码(行政区划代码)")
    private String cityCode;

    @Schema(description ="所属省份ID")
    private Long provinceId;

    @Schema(description ="创建时间")
    private Date createTime;

    @Schema(description ="更新时间")
    private Date updateTime;
}
