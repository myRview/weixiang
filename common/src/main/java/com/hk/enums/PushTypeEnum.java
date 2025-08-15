package com.hk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * webscoket类型枚举
 *
 * @author huangkun
 */
@Getter
@AllArgsConstructor
public enum PushTypeEnum {
    //系统消息
    SYSTEM("system", "系统消息"),
    //用户消息
    USER("user", "用户消息"),
    //文章消息
    ARTICLE("article", "文章消息"),
    //评论消息
    COMMENT("comment", "评论消息"),
    //订单消息
    ORDER("order", "订单消息"),
    ;
    private final String code;
    private final String description;

    /**
     * 根据code获取
     *
     * @param code
     * @return
     */
    public static PushTypeEnum getByCode(String code) {
        for (PushTypeEnum typeEnum : values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum;
            }
        }
        return null;
    }
}
