package com.hk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author huangkun
 * @date 2025/7/21 9:50
 */
@Getter
@AllArgsConstructor
public enum FileCategoryEnum {
    IMAGE("image", "图片"),
    VIDEO("video", "视频"),
    AUDIO("audio", "音频"),
    DOCUMENT("document", "文档"),
    ;

    private String code;
    private String description;

    public static FileCategoryEnum getByCode(String code) {
        for (FileCategoryEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
