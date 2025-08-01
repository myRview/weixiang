package com.hk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author huangkun
 * @date 2025/7/30 10:41
 */
@Getter
@AllArgsConstructor
public enum ArticleAuditStatus {
    PENDING(0, "待审核"),
    PASS(1, "审核通过"),
    REJECT(2, "审核拒绝"),
    ;
    private final int code;
    private final String description;

    public static ArticleAuditStatus getByCode(int code) {
        for (ArticleAuditStatus status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null;
    }
}
