package com.zjquincy.ncu.net.response;

import com.google.gson.annotations.SerializedName;

/*
 * 登录响应格式：
 * String response_type = "CHANGE_PASSWORD"
 * String message 信息，值见构造方法
 * */
public class ChangePasswordResponse extends AbstractResponse {
    @SerializedName("message")
    private String message;

    @Override
    public String getLogMessage() {
        return message;
    }

    public enum Result {
        WRONG_OLD_PASSWORD,
        SUCCESS,
        ERROR
    }

    private ChangePasswordResponse(){
        response_type = "CHANGE_PASSWORD";
    }
    public ChangePasswordResponse(Result result) {
        this();
        message=result.name();
    }
}
