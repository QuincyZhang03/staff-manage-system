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

    public enum Result {
        WRONG_OLD_PASSWORD,
        SUCCESS,
        ERROR
    }

    public ChangePasswordResponse(Result result) {
        response_type = "CHANGE_PASSWORD";
        message=result.name();
    }
}
