package com.hk.utils;

import cn.hutool.crypto.SecureUtil;

/**
 * @author huangkun
 * @date 2025/6/26 16:50
 */
public class Md5Utils {

    public static String md5(String salt, String password) {
        String md5 = SecureUtil.md5(salt + password);
        return md5;
    }
}
