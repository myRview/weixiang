package com.hk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author huangkun
 * @date 2025/7/30 10:41
 */
@Getter
@AllArgsConstructor
public enum ArticlePublishStatus {

    PUBLISHED(1, "已发布"),
    NOT_PUBLISHED(0, "未发布"),
    ;

    private final int code;
    private final String description;
}
