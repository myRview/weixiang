package com.hk.utils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author huangkun
 * @date 2025/7/6 10:39
 */
public class IPUtil {
    public static String getClientIp(HttpServletRequest request) {
        // 定义可能包含客户端IP的请求头
        String[] headers = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED",
                "HTTP_VIA",
                "X-Real-IP"
        };

        String ip = null;

        // 按顺序检查请求头，直到找到有效的IP地址
        for (String header : headers) {
            ip = request.getHeader(header);
            if (isValidIp(ip)) {
                break;
            }
        }

        // 如果所有请求头都无效，则使用远程地址
        if (!isValidIp(ip)) {
            ip = request.getRemoteAddr();
        }

        // 处理X-Forwarded-For包含多个IP的情况（取第一个有效IP）
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        // 处理本地地址返回IPv6格式的问题
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }

        return ip;
    }

    /**
     * 验证IP地址是否有效
     *
     * @param ip IP地址字符串
     * @return 有效返回true，否则false
     */
    private static boolean isValidIp(String ip) {
        return ip != null &&
                ip.length() != 0 &&
                !"unknown".equalsIgnoreCase(ip) &&
                !ip.startsWith("0:");
    }

}
