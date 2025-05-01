package com.zjquincy.ncu.net.request;

import com.google.gson.annotations.SerializedName;

public class QueryRequest extends AbstractRequest{
    /*
     * 查询请求格式：
     * String request_type = "QUERY"
     * String username 用户名
     * */
    @SerializedName("username")
    private String username;

    public String getUsername() {
        return username;
    }
}
