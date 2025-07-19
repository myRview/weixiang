package com.hk.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

public class RequestUtils {

    public static Map<String, Object> getRequestParams(HttpServletRequest request, JoinPoint joinPoint) {
        Map<String, Object> params = new HashMap<>();
        
        // 获取URL参数
        Map<String, String[]> urlParams = request.getParameterMap();
        if (!urlParams.isEmpty()) {
            urlParams.forEach((key, values) -> 
                params.put(key, values.length == 1 ? values[0] : values)
            );
        }
        
        // 获取Body参数（排除文件类型）
        if (joinPoint.getArgs() != null) {
            for (Object arg : joinPoint.getArgs()) {
                if (arg instanceof MultipartFile || arg instanceof HttpServletRequest) {
                    continue;
                }
                try {
                    params.put("body", arg);
                } catch (Exception ignored) {}
            }
        }
        
        return params;
    }
}