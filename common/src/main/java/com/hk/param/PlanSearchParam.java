package com.hk.param;

import com.hk.common.PageBaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author huangkun
 * @date 2025/6/27 11:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PlanSearchParam extends PageBaseVO {


    @Schema(description = "套餐名称")
    private String name;

    @Schema(description = "0-禁用，1-启用")
    private Integer status;
}
