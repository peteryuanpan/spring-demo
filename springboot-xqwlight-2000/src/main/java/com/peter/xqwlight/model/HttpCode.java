package com.peter.xqwlight.model;

public enum HttpCode {

    SUCCESS(200, "请求成功"),
    LOGIN_AUTH_FAIL(401, "login认证失败"),
    JWT_AUTH_FAIL(401, "JWT认证失败"),
    ACCESS_DENIED(403, "请求无权限")
    ;

    private int code;
    private String message;

    HttpCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
