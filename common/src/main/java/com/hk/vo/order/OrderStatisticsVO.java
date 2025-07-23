package com.hk.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author huangkun
 * @date 2025/7/22 9:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatisticsVO implements Serializable {

    private static final long serialVersionUID = -249798794221536482L;

    @Schema(description = "数量")
    private Integer total;

    @Schema(description = "订单状态,0未支付，1-已支付,2-已取消")
    private Integer status;

    @Schema(description = "订单金额")
    private BigDecimal totalAmount;

}
