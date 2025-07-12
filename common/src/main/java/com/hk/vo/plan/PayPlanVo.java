package com.hk.vo.plan;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author huangkun
 * @date 2025/7/7 13:43
 */
@Data
public class PayPlanVo {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "套餐id")
    private Long planId;

}
