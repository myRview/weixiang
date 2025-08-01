package com.hk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态枚举
 *
 * @author huangkun
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {

    NORMAL(1, "正常"),
    DISABLE(0, "禁用"),
    ;
    private final int code;
    private final String description;

    /**
     * 根据code获取
     *
     * @param code
     * @return
     */
    public static StatusEnum getByCode(int code) {
        for (StatusEnum value : StatusEnum.values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }
}
