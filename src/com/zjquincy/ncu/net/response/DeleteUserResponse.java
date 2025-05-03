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

    private DeleteUserResponse(){
        response_type = "DELETE_USER";
    }
    public DeleteUserResponse(String message) {
        this();
        this.message = message;
    }
}