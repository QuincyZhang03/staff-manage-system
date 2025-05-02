package com.zjquincy.ncu.net.response;

import com.google.gson.annotations.SerializedName;

/*
 * 删除用户数据响应格式：
 * String response_type = "DELETE_USER"
 * String message 删除结果信息文本
 * */
public class DeleteUserResponse extends AbstractResponse{
    @SerializedName("message")
    private String message;
    public DeleteUserResponse(String message) {
        response_type = "DELETE_USER";
        this.message = message;
    }
}