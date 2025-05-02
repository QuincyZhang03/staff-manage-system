package com.zjquincy.ncu.net.response;

import com.google.gson.annotations.SerializedName;

/*
 * 删除数据响应格式：
 * String response_type = "DELETE"
 * String message 删除结果信息文本
 * */
public class DeleteResponse extends AbstractResponse{
    @SerializedName("message")
    private String message;
    public DeleteResponse(String message) {
        response_type = "DELETE";
        this.message = message;
    }
}