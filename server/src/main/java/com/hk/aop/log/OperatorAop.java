package com.hk.aop.log;

import com.hk.aop.log.annotation.OperatorLog;
import com.hk.context.UserContext;
import com.hk.entity.log.OperationLogEntity;
import com.hk.service.log.OperationLogService;
import com.hk.utils.IPUtil;
import com.hk.utils.RequestUtils;
import com.hk.vo.log.OperationLogVO;
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

/**
 * @author huangkun
 * @date 2025/7/6 10:54
 */
@Aspect
@Component
@Slf4j
public class OperatorAop {

    @Autowired
    private OperationLogService operationLogService;


    @Pointcut("@annotation(com.hk.aop.log.annotation.OperatorLog)")
    public void operatorPointCut() {
    }

    @Around("operatorPointCut()")
    public Object recordOperatorLog(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法签名和注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        OperatorLog operatorLog = method.getAnnotation(OperatorLog.class);
        OperationLogVO operationLog = new OperationLogVO();
        operationLog.setOperationContent(operatorLog.desc());
        operationLog.setOperationModule(operatorLog.value());
        operationLog.setOperationTime(new Date());
        operationLog.setStatus(1);
        // 获取请求信息
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();
        String params = RequestUtils.getRequestParams(request, joinPoint);
        log.error("请求参数:{}", params);
        String ip = IPUtil.getClientIp(request);
        operationLog.setIpAddress(ip);
        operationLog.setUserId(UserContext.getCurrentUserId());
        operationLog.setUsername(UserContext.getCurrentUsername());
        operationLog.setOperationAddress(request.getRequestURI());
        Object result = null;
        try {
            // 执行目标方法
            result = joinPoint.proceed();
        } catch (Exception e) {
            operationLog.setStatus(0);
            log.error("目标方法执行异常:{}", e);
        } finally {
            // 保存日志
            operationLogService.addLog(operationLog);
        }
        return result;
    }

}
