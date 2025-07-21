package com.hk.interceptor;

import cn.hutool.json.JSONUtil;
import com.hk.common.ErrorCode;
import com.hk.common.ResponseResult;
import com.hk.constants.BaseConstant;
import com.hk.context.UserContext;
import com.hk.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * @author huangkun
 * @date 2025/7/6 14:16
 */
@Component
@Slf4j
@Order(1)
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取当前请求是否携带token
        String token = request.getHeader(BaseConstant.TOKEN);
        if (StringUtils.isBlank(token)) {
            log.error("token为空");
            sendErrorResponse(response);
            return false;
        }
        try {
            JwtUtil.parseToken(token);
        } catch (Exception e) {
            log.error("token解析失败", e);
            sendErrorResponse(response);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    private  void sendErrorResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ResponseResult<String> result = new ResponseResult<>();
        result.setCode(ErrorCode.NOT_LOGIN.getCode());
        result.setMessage(ErrorCode.NOT_LOGIN.getMessage());
        response.getWriter().write(JSONUtil.toJsonStr(result));
    }
}
