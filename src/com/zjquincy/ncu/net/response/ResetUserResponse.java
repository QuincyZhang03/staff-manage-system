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
    public ResetUserResponse(String message) {
        response_type = "RESET_USER";
        this.message = message;
    }
}
