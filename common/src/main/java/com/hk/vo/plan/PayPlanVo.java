package com.hk.vo.plan;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author huangkun
 * @date 2025/7/7 13:43
 */
@Data
public class PayPlanVo {

    @Schema(description = "用户id")
    @NotNull(message = "用户id不能为空")
    private Long userId;

    @Schema(description = "套餐id")
    @NotNull(message = "套餐id不能为空")
    private Long planId;

}
