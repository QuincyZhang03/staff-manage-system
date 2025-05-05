package com.zjquincy.ncu.net.response;

import com.google.gson.annotations.SerializedName;

/*
 * 注册响应格式：
 * String response_type = "REGISTER"
 * String message 信息，值见构造方法
 * */
public class RegisterResponse extends AbstractResponse {
    @SerializedName("message")
    private String message;

    public enum Result {
        USER_ALREADY_EXISTS,
        SUCCESS,
        ERROR
    }

    @Override
    public String getLogMessage() {
        return message;
    }

    private RegisterResponse(){
        response_type = "REGISTER";
    }
    public RegisterResponse(Result result) {
        this();
        message=result.name();
    }
}
