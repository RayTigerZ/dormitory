package com.ray.dormitory.util.bean;

public enum ErrorEnum {

    ERROR_204(204, "请求处理异常"),

    ERROR_201(201, "账号权限不足"),

    ERROR_500(500, "系统内部错误"),

    ERROR_302(302, "登录Token过期"),

    ERROR_301(301, "未登录或登录超时"),
    ERROR_303(303, "账号在其他地方登陆");


    private Integer errorCode;
    private String errorMsg;

    ErrorEnum(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}