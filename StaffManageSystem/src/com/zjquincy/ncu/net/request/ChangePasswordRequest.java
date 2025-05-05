package com.zjquincy.ncu.net.request;

import com.google.gson.annotations.SerializedName;
import com.sun.net.httpserver.HttpExchange;
import com.zjquincy.ncu.access.PasswordVerifier;
import com.zjquincy.ncu.access.User;
import com.zjquincy.ncu.net.NetUtility;
import com.zjquincy.ncu.net.response.ChangePasswordResponse;

import java.io.IOException;
import java.sql.*;

import static com.zjquincy.ncu.Entry.*;

/*
 * 修改密码请求Json格式：
 * String request_type = "CHANGE_PASSWORD"
 * String username 用户名
 * String old_password 旧密码明文
 * String new_password 新密码明文
 * */
public class ChangePasswordRequest extends AbstractRequest {
    @SerializedName("username")
    private String username;
    @SerializedName("old_password")
    private String old_password;
    @SerializedName("new_password")
    private String new_password;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.printf("请求编号%d解析成功：来自%s的修改密码请求\n", requestID, username);
        try {
            PasswordVerifier.Result result = PasswordVerifier.verifyPassword(username, old_password);
            if (result.getType() == PasswordVerifier.ResultType.NO_SUCH_USER) { //修改不存在的用户的密码，出现错误
                NetUtility.sendResponse(exchange, new ChangePasswordResponse(ChangePasswordResponse.Result.ERROR),requestID);
            } else if (result.getType() == PasswordVerifier.ResultType.WRONG) {//旧密码错误
                NetUtility.sendResponse(exchange, new ChangePasswordResponse(ChangePasswordResponse.Result.WRONG_OLD_PASSWORD),requestID);
            } else {//旧密码正确，执行修改密码操作
                User user = result.getUser();
                Connection connection = DriverManager.getConnection(DB_URL, user.getDBUsername(), user.getDBPassword());
                String encryptedPassword = PasswordVerifier.getEncryptedPassword(username, result.getUser().getTimestamp(), new_password);
                PreparedStatement statement = connection.prepareStatement("UPDATE visitor SET password=? WHERE username=?");
                statement.setString(1, encryptedPassword);
                statement.setString(2, username);
                if (statement.executeUpdate() == 1) {
                    NetUtility.sendResponse(exchange, new ChangePasswordResponse(ChangePasswordResponse.Result.SUCCESS),requestID);
                } else {//如果修改了超过1个用户，也叫发生错误了
                    NetUtility.sendResponse(exchange, new ChangePasswordResponse(ChangePasswordResponse.Result.ERROR),requestID);
                }
                statement.close();
                connection.close();
            }
        } catch (SQLException e) {
            NetUtility.sendResponse(exchange, new ChangePasswordResponse(ChangePasswordResponse.Result.ERROR),requestID);
            throw new RuntimeException(e);
        }
    }
}
