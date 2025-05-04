package com.zjquincy.ncu.net.response;

import com.google.gson.annotations.SerializedName;

/*
 * 重置用户密码响应格式：
 * String response_type = "RESET_USER"
 * String message 重置结果信息文本
 * */
public class ResetUserResponse extends AbstractResponse{
    @SerializedName("message")
    private String message;

    private ResetUserResponse(){
        response_type = "RESET_USER";
    }
    public ResetUserResponse(String message) {
        this();
        this.message = message;
    }
}
