package com.lab.meet.Model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
@ApiModel(description = "返回响应数据")
public class ResponseBody<T> {
    public final static Integer OK = 0;
    public final static Integer ERROR = 500;

    @ApiModelProperty(value = "是否成功")
    private boolean success;
    @ApiModelProperty(value = "返回代码")
    private Integer code;
    @ApiModelProperty(value = "错误信息")
    private String msg;
    @ApiModelProperty(value = "返回对象")
    private T data;

    public static <T> ResponseBody getRep(Status status, T data) {
        int code = status.getCode();
        String msg = status.getMsg();
        return new ResponseBody<>(code == OK, code, msg, data);
    }

    public static <T> ResponseBody successRep(T data) {
        Status status = Status.GENERATESUCCESS;
        int code = status.getCode();
        String msg = status.getMsg();
        return new ResponseBody<>(true, code, msg, data);
    }

    public static <T> ResponseBody failedRep(T data) {
        Status status = Status.FAILED;
        int code = status.getCode();
        String msg = status.getMsg();
        return new ResponseBody<>(false, code, msg, data);
    }

    public static ResponseBody failedRep() {
        Status status = Status.FAILED;
        int code = status.getCode();
        String msg = status.getMsg();
        return new ResponseBody<>(false, code, msg, new ArrayList<>());
    }

    public static ResponseBody failedRep(Status status) {
        int code = status.getCode();
        String msg = status.getMsg();
        return new ResponseBody<>(false, code, msg, new ArrayList<>());
    }

    public static ResponseBody failedRep(String msg, int code) {
        Status status = Status.getCostum(msg, code);
        return ResponseBody.failedRep(status);
    }

    private ResponseBody(boolean success, Integer code, String msg, T data) {
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
