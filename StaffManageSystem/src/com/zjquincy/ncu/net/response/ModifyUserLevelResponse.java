package com.zjquincy.ncu.net.response;

import com.google.gson.annotations.SerializedName;

/*
 * 变更用户等级响应格式：
 * String response_type = "MODIFY_USER_LEVEL"
 * String message 重置结果信息文本
 * */
public class ModifyUserLevelResponse extends AbstractResponse{
    @SerializedName("message")
    private String message;

    @Override
    public String getLogMessage() {
        return message;
    }

    private ModifyUserLevelResponse(){
        response_type = "MODIFY_USER_LEVEL";
    }
    public ModifyUserLevelResponse(String message) {
        this();
        this.message = message;
    }
}