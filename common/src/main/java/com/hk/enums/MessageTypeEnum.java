package com.hk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author huangkun
 * @date 2025/7/15 14:06
 */
@Getter
@AllArgsConstructor
public enum MessageTypeEnum {

    //订单
    ORDER_CREATE("ORDER_CREATE", "订单创建"),
    ORDER_COMPLETE("ORDER_COMPLETE", "订单完成"),

    ;
    private String code;
    private String desc;

    public static MessageTypeEnum getByCode(String code) {
        for (MessageTypeEnum value : MessageTypeEnum.values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }
}
