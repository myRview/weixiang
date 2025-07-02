package com.hk.exception;

import com.hk.common.ErrorCode;
import com.hk.common.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    @ExceptionHandler(Exception.class)
    public ResponseResult handleException(Exception e) {
        log.error("Exception={}", e);
        return ResponseResult.fail(ErrorCode.ERROR_SYSTEM);
    }
}
