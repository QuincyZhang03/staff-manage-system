package com.zjquincy.ncu.net.request;

import com.google.gson.annotations.SerializedName;
import com.zjquincy.ncu.access.SHA256;

import java.sql.Timestamp;

/*
 * 注册请求格式：
 * String request_type = "REGISTER"
 * String username 用户名
 * String input_password 密码明文
 * */
public class RegisterRequest extends AbstractRequest {
    @SerializedName("username")
    private String username;
    @SerializedName("input_password")
    private String input_password;

    public String getUsername() {
        return username;
    }

    public String getEncryptedPassword(Timestamp timestamp) {
        return SHA256.convert(String.format("%s@%s@%s", username, timestamp, input_password));
    }
}
