package com.hk.entity.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author hk
 * @since 2025-06-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "OrderInfoEntity对象", description = "订单表")
@TableName("order_info")
public class OrderInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.ASSIGN_ID)
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

     @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "套餐名")
    private String planName;


}