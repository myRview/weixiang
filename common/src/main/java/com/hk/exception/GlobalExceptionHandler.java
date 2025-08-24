package com.hk.exception;

import com.hk.common.ErrorCode;
import com.hk.common.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLException;

/**
 * @author huangkun
 * @date 2025/6/29 10:00
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(BusinessException.class)
    public ResponseResult handleException(BusinessException e) {
        log.error("BusinessException={}", e.getMessage());
        return ResponseResult.fail(e.getCode(), e.getMessage());
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseResult handleException(AccessDeniedException e) {
        log.error("AccessDeniedException={}", e.getMessage());
        return ResponseResult.fail(ErrorCode.NO_PERMISSION);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseResult handleException(SQLException e) {
        log.error("SQLException={}", e.getMessage());
        return ResponseResult.fail(ErrorCode.ERROR_SYSTEM,"操作数据库异常");
    }
    // 处理静态资源不存在异常
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNoResourceFoundException(NoResourceFoundException e) {
        log.warn("Static resource not found: {}", e);
    }
    @ExceptionHandler(Exception.class)
    public ResponseResult handleException(Exception e) {
        log.error("Exception={}", e);
        return ResponseResult.fail(ErrorCode.ERROR_SYSTEM);
    }
}
