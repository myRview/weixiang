package com.hk.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一响应结果封装类
 *
 * @author huangkun
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResult<T> {

    private Integer code;
    private String message;
    private T data;

    /**
     * 成功响应（无数据，使用默认消息）
     */
    public static ResponseResult<?> success() {
        return new ResponseResult<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), null);
    }

    /**
     * 成功响应（自定义消息）
     */
    public static ResponseResult<?> success(String message) {
        return new ResponseResult<>(ErrorCode.SUCCESS.getCode(), message, null);
    }

    /**
     * 成功响应（带数据，使用默认消息）
     */
    public static <T> ResponseResult<T> success(T data) {
        return success(data, ErrorCode.SUCCESS.getMessage());
    }

    /**
     * 成功响应（带数据和自定义消息）
     */
    public static <T> ResponseResult<T> success(T data, String message) {
        return new ResponseResult<>(ErrorCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败响应（使用系统错误默认消息）
     */
    public static ResponseResult<?> fail() {
        return fail(ErrorCode.ERROR_SYSTEM);
    }

    /**
     * 失败响应（自定义消息）
     */
    public static ResponseResult<?> fail(String message) {
        return fail(ErrorCode.ERROR_SYSTEM, message);
    }

    /**
     * 失败响应（指定错误码）
     */
    public static <T> ResponseResult<T> fail(ErrorCode errorCode) {
        return fail(errorCode, errorCode.getMessage());
    }

    /**
     * 失败响应（指定错误码和自定义消息）
     */
    public static <T> ResponseResult<T> fail(ErrorCode errorCode, String message) {
        return fail(errorCode, message, null);
    }

    /**
     * 失败响应（指定错误码、自定义消息和数据）
     */
    public static <T> ResponseResult<T> fail(ErrorCode errorCode, String message, T data) {
        return fail(errorCode.getCode(), message, data);
    }

    /**
     * 失败响应（指定状态码和消息）
     */
    public static <T> ResponseResult<T> fail(Integer code, String message) {
        return fail(code, message, null);
    }

    /**
     * 失败响应（完整参数）
     */
    public static <T> ResponseResult<T> fail(Integer code, String message, T data) {
        return new ResponseResult<>(code, message, data);
    }
}