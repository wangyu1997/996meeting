package com.lab.meet.Model;

public enum Status {
    GENERATESUCCESS("", 0),
    SUCCESS("请求成功", 0),
    FAILED("请求失败", 500),
    LOGIN_SUCCESS("登陆成功", 0),
    LOGIN_FAILED("登陆失败", 10),
    TOKEN_REFRESH_FAILED("TOKEN刷新失败", 20),
    TOKEN_REFRESH_SUCCESS("TOKEN刷新成功", 0),
    PASSWORD_NOT_NULL("密码不能为空", 30),
    USER_NOT_EXIST("该用户不存在", 11),
    FILE_NOT_EXIST("请至少选择一个文件", 12),
    USER_NOT_LOGIN("用户未登录", 403),
    CAN_NOT_ACCESS("您没有权限做此操作", 1098),
    COSTUM_STATUS("", 10099);

    private String msg;
    private int code;

    Status(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public static Status getCostum(String msg, int code) {
        COSTUM_STATUS.msg = msg == null ? "" : msg;
        COSTUM_STATUS.code = code;
        return COSTUM_STATUS;
    }

}