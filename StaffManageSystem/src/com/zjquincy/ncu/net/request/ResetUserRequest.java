package com.zjquincy.ncu.net.request;

import com.google.gson.annotations.SerializedName;
import com.sun.net.httpserver.HttpExchange;
import com.zjquincy.ncu.access.PasswordVerifier;
import com.zjquincy.ncu.access.User;
import com.zjquincy.ncu.net.NetUtility;
import com.zjquincy.ncu.net.response.ResetUserResponse;
import java.io.IOException;
import java.sql.*;

import static com.zjquincy.ncu.Entry.DB_URL;

/*
 * 重置用户密码请求格式：
 * String request_type = "RESET_USER"
 * String username 用户名
 * String to_reset 要被重置的用户名
 */
public class ResetUserRequest extends AbstractRequest {
    @SerializedName("username")
    private String username;
    @SerializedName("to_reset")
    private String to_reset;

    private static final String DEFAULT_PASSWORD = "123456";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.printf("请求编号%d解析成功：来自%s的重置用户密码请求\n",requestID, username);
        try {
            User user = User.fetchUser(username);
            User resetUser = User.fetchUser(to_reset);
            if (user == null) {
                NetUtility.sendResponse(exchange, new ResetUserResponse("未找到用户：" + username),requestID);//一般情况下不会出现这种情况
            } else if (resetUser == null) {
                NetUtility.sendResponse(exchange, new ResetUserResponse("未找到用户：" + to_reset),requestID);
            } else if (user.getLevel() < User.OPERATOR) {//用户权限不足，不能重置其他用户密码
                NetUtility.sendResponse(exchange, new ResetUserResponse("权限不足！"),requestID);
            } else if (user.getLevel() <= resetUser.getLevel()) {//只能上重置下
                NetUtility.sendResponse(exchange, new ResetUserResponse(
                        String.format("%s(等级%d)的权限等级不低于您，不能重置ta的密码", to_reset, resetUser.getLevel())),requestID);
            } else {//将用户密码重置为123456
                Connection connection = DriverManager.getConnection(DB_URL, user.getDBUsername(), user.getDBPassword());
                PreparedStatement statement = connection.prepareStatement("UPDATE visitor SET password=? WHERE username=?");
                String encryptedPassword = PasswordVerifier.getEncryptedPassword(to_reset, resetUser.getTimestamp(), DEFAULT_PASSWORD);
                statement.setString(1, encryptedPassword);
                statement.setString(2, to_reset);
                if (statement.executeUpdate() == 1) {//如果修改了超过1个用户，也叫发生错误了
                    NetUtility.sendResponse(exchange, new ResetUserResponse("重置成功！"),requestID);
                } else {
                    NetUtility.sendResponse(exchange, new ResetUserResponse("重置失败！"),requestID);
                }
                statement.close();
                connection.close();
            }
        } catch (SQLException e) {
            NetUtility.sendResponse(exchange, new ResetUserResponse("未知错误：" + e.getMessage()),requestID);
            throw new RuntimeException(e);
        }
    }
}
