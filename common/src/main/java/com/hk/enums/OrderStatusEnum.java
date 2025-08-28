package com.hk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单状态枚举
 *
 * @author huangkun
 */
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    /**
     * 待支付
     */
    WAIT_PAY(0, "待支付"),
    /**
     * 已完成
     */
    FINISHED(1, "已支付"),
    /**
     * 已取消
     */
    CANCEL(2, "已取消"),
    /**
     * 退款
     */
    REFUND(3, "退款");

    ;
    private final int code;
    private final String description;

    /**
     * 根据code获取
     *
     * @param code
     * @return
     */
    public static OrderStatusEnum getByCode(int code) {
        for (OrderStatusEnum value : OrderStatusEnum.values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }
}
