package com.hk.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * @author huangkun
 * @date 2025/8/17 10:46
 */
public class CheckUtil {
    //手机号正则表达式（中国大陆11位）
    private static final String PHONE_REGEX = "^1[3-9]\\d{9}$";
    //邮箱正则表达式（支持常见格式）
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    public static boolean checkPhone(String phone) {
        return StringUtils.isNotBlank(phone) && Pattern.matches(PHONE_REGEX, phone.trim());
    }
    public static boolean checkEmail(String email) {
        return StringUtils.isNotBlank(email) && Pattern.matches(EMAIL_REGEX, email.trim());
    }
}
