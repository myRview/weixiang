package com.hk.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author huangkun
 * @date 2025/7/18 14:01
 */
@Data
public class AliPayVO {

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "订单id")
    private Long orderId;

    @Schema(description = "订单金额")
    private BigDecimal amount;

    @Schema(description = "订单主题")
    private String subject;
}
