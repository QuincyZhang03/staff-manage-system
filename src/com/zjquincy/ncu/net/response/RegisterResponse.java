package com.zjquincy.ncu.net.response;

import com.google.gson.annotations.SerializedName;

/*
 * 注册响应格式：
 * String response_type = "register"
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
        response_type = "register";
        if (result == Result.USER_ALREADY_EXISTS) {
            message = "user_already_exists";
        } else if (result == Result.SUCCESS) {
            message = "success";
        } else if (result == Result.ERROR) {
            message = "error";
        }
        //理论上不会出现再else的情况
    }
}
