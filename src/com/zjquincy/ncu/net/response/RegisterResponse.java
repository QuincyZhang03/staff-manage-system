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

    public RegisterResponse(Result result) {
        response_type = "REGISTER";
        message=result.name();
    }
}
