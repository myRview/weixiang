package com.hk.aop.log;

import com.hk.aop.log.annotation.LoginLog;
import com.hk.context.UserContext;
import com.hk.enums.StatusEnum;
import com.hk.service.log.LoginLogService;
import com.hk.utils.IPUtil;
import com.hk.utils.RequestUtils;
import com.hk.vo.log.LoginLogVO;
import com.hk.vo.user.UserLoginVO;
import com.hk.vo.user.UserRegisterVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * @author huangkun
 * @date 2025/7/8 9:43
 */
@Aspect
@Component
@Slf4j
public class LoginAop {

    @Autowired
    private LoginLogService loginLogService;

    @Pointcut("@annotation(com.hk.aop.log.annotation.LoginLog)")
    public void loginPointCut() {
    }

    @Around("loginPointCut()")
    public Object recordLoginLog(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LoginLog loginLog = method.getAnnotation(LoginLog.class);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String value = loginLog.value();
        String userName = "";
        Long userId = 0L;
        Map<String, Object> requestParams = RequestUtils.getRequestParams(request, joinPoint);
        Object body = requestParams.get("body");
        switch (value) {
            case "login":
                UserLoginVO loginVO = (UserLoginVO) body;
                userName = loginVO.getAccount();
                break;
            case "register":
                UserRegisterVO registerVO = (UserRegisterVO) body;
                userName = registerVO.getAccount();
                break;
            case "logout":
                userName = UserContext.getCurrentUsername();
                userId = UserContext.getCurrentUserId();
        }
        LoginLogVO loginLogVO = new LoginLogVO();
        loginLogVO.setUsername(userName);
        loginLogVO.setUserId(userId);
        String ip = IPUtil.getClientIp(request);
        loginLogVO.setIpAddress(ip);
        IPUtil.LocationInfo location = IPUtil.getLocationByIp(ip);
        loginLogVO.setLocation(location.city());
        IPUtil.DeviceInfo deviceInfo = IPUtil.getDeviceInfo(request);
        loginLogVO.setDevice(deviceInfo.os());
        loginLogVO.setLoginTime(new Date());
        loginLogVO.setStatus(StatusEnum.NORMAL.getCode());
        Object result = null;
        try {
            // 执行目标方法
            result = joinPoint.proceed();
        } catch (Exception e) {
            loginLogVO.setStatus(StatusEnum.DISABLE.getCode());
            log.error("目标方法执行异常:{}", e);
        } catch (Throwable e) {
            loginLogVO.setStatus(StatusEnum.DISABLE.getCode());
            log.error("目标方法执行异常:{}", e);
        } finally {
            // 保存日志
            loginLogService.addLog(loginLogVO);
        }
        return result;
    }
}
