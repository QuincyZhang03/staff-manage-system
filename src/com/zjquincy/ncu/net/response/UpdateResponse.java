package com.zjquincy.ncu.net.response;

import com.google.gson.annotations.SerializedName;

/*
 * 修改数据响应格式：
 * String response_type = "UPDATE"
 * String message 修改结果信息文本
 * */
public class UpdateResponse extends AbstractResponse {
    @SerializedName("message")
    private String message;

    public UpdateResponse(String message) {
        response_type = "UPDATE";
        this.message = message;
    }
}
