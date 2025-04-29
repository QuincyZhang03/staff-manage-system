package com.zjquincy.ncu.net.response;

import com.google.gson.annotations.SerializedName;
import com.zjquincy.ncu.access.User;

/*
 * 登录响应格式：
 * String response_type = "login"
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
        response_type = "login";
        if (result == Result.NO_SUCH_USER) {
            message = "no_such_user";
        } else if (result == Result.VERIFIED) {
            message = "verified";
        } else if (result == Result.WRONG_PASSWORD) {
            message = "wrong_password";
        } else if (result == Result.ERROR) {
            message = "error";
        }
        //理论上不会出现再else的情况
    }

    public LoginResponse(User user) {
        this(Result.VERIFIED);
        user_info = user;
    }
}
