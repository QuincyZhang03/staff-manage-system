package com.zjquincy.ncu.net.response;

import com.google.gson.annotations.SerializedName;

/*
 * 新增数据响应格式：
 * String response_type = "CREATE"
 * String message 创建结果信息文本
 * */
public class CreateResponse extends AbstractResponse {
    @SerializedName("message")
    private String message;

    @Override
    public String getLogMessage() {
        return message;
    }

    private CreateResponse(){
        response_type = "CREATE";
    }
    public CreateResponse(String message) {
        this();
        this.message = message;
    }
}
