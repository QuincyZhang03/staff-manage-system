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

    @Override
    public String getLogMessage() {
        return message;
    }

    private UpdateResponse(){
        response_type = "UPDATE";
    }
    public UpdateResponse(String message) {
        this();
        this.message = message;
    }
}
