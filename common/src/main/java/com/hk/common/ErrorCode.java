package com.hk.common;

import lombok.Getter;

/**
 * 自定义错误
 *
 * @author huangkun
 */
@Getter
public enum ErrorCode {
    SUCCESS(200, "操作成功"),
    CREATED(201, "资源创建成功"),
    ACCEPTED(202, "请求已接受"),
    NO_CONTENT(204, "无内容返回"),

    BAD_REQUEST(400, "请求参数错误"),
    NOT_LOGIN(401, "未登录或认证失败"),
    NO_PERMISSION(403, "无访问权限"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),

    ERROR_SYSTEM(500, "系统内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),

    BUSINESS_ERROR(1000, "业务逻辑错误"),
    VALIDATION_ERROR(1001, "数据验证失败"),
    REMOTE_SERVICE_ERROR(1002, "远程服务调用失败");


    private final int code;

    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
