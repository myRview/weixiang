package com.hk.filter;

import cn.hutool.json.JSONUtil;
import com.hk.common.ResponseResult;
import com.hk.utils.BlackIpUtil;
import com.hk.utils.IPUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * @author huangkun
 * @date 2025/8/25 14:03
 */
@WebFilter(urlPatterns = "/*")
public class BackIpFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String ip = IPUtil.getClientIp(httpServletRequest);
        if(BlackIpUtil.isBlackIp(ip)) {
            servletResponse.setContentType("text/json;charset=utf-8");
            servletResponse.getWriter().write(JSONUtil.toJsonStr(ResponseResult.fail("你已被拉入黑名单")));
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
