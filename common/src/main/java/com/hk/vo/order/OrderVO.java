package com.hk.vo.order;

import com.hk.entity.order.OrderInfoEntity;
import com.hk.vo.plan.MemberPlanVO;
import com.hk.vo.user.UserVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * @author huangkun
 * @date 2025/6/27 14:30
 */
@Data
public class OrderVO {

    @Schema(description = "订单id")
    private Long id;

    @Schema(description = "订单编号")
    private String orderNumber;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "套餐id")
    private Long planId;

    @Schema(description = "下单日期")
    private Date orderDate;

    @Schema(description = "订单金额")
    private BigDecimal amount;

    @Schema(description = "订单状态,0未支付，1-已支付")
    private Integer status;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "用户信息")
    private UserVO userVO;

    @Schema(description = "套餐信息")
    private MemberPlanVO planVO;

    public static OrderVO converter(OrderInfoEntity orderInfo) {
        OrderVO orderVO = new OrderVO();
        orderVO.setId(orderInfo.getId());
        orderVO.setOrderNumber(orderInfo.getOrderNumber());
        orderVO.setUserId(orderInfo.getUserId());
        orderVO.setPlanId(orderInfo.getPlanId());
        orderVO.setOrderDate(orderInfo.getOrderDate());
        orderVO.setAmount(orderInfo.getAmount());
        orderVO.setStatus(orderInfo.getStatus());
        orderVO.setCreateTime(orderInfo.getCreateTime());
        return orderVO;
    }
}
