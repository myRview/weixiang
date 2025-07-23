package com.hk.vo.plan;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author huangkun
 * @date 2025/7/22 13:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanStatisticsVO implements Serializable {
    private static final long serialVersionUID = 2658465299752808169L;

    @Schema(description = "套餐名称")
    private String planName;

    @Schema(description = "购买数量")
    private Integer total;

}
