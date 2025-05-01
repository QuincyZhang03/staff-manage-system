package com.zjquincy.ncu.net.response;

import com.google.gson.annotations.SerializedName;
import com.zjquincy.ncu.access.User;

/*
 * 登录响应格式：
 * String response_type = "LOGIN"
 * String message 信息，值见构造方法
 * Json user_info 登录成功后返回账户信息json，登录失败无该字段
 * */
public class LoginResponse extends AbstractResponse {
    @SerializedName("message")
    private String message;
    @SerializedName("user_info")
    private User user_info;

    public enum Result {
        NO_SUCH_USER,
        WRONG_PASSWORD,
        VERIFIED,
        ERROR
    }

    public LoginResponse(Result result) {
        response_type = "LOGIN";
        message=result.name();
    }

    public LoginResponse(User user) {
        this(Result.VERIFIED);
        user_info = user;
    }
}
