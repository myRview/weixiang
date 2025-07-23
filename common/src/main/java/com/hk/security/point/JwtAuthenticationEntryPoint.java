package com.hk.security.point;

import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.hk.common.ResponseResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * JWT认证失败处理
 * @author huangkun
 * @date 2025/7/23 9:03
 */
@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("JWT认证失败，无法访问系统资源，请求访问：{},异常信息：{}", request.getRequestURI(), authException.getMessage());
        response.setStatus(HttpStatus.HTTP_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().print(JSONUtil.toJsonStr(ResponseResult.fail("用户未登录")));
    }
}
