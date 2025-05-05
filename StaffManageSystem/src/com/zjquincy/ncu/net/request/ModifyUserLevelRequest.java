package com.zjquincy.ncu.net.request;

import com.google.gson.annotations.SerializedName;
import com.sun.net.httpserver.HttpExchange;
import com.zjquincy.ncu.Entry;
import com.zjquincy.ncu.access.User;
import com.zjquincy.ncu.net.NetUtility;
import com.zjquincy.ncu.net.response.ModifyUserLevelResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
 * 变更用户等级请求格式：
 * String request_type = "MODIFY_USER_LEVEL"
 * String username 用户名
 * String to_modify 要被变更的用户名
 * int new_level 新等级
 * String key 敏感权限密钥
 */
public class ModifyUserLevelRequest extends AbstractRequest {
    @SerializedName("username")
    private String username;
    @SerializedName("to_modify")
    private String to_modify;
    @SerializedName("new_level")
    private int new_level;
    @SerializedName("key")
    private String key;

    private static final String KEYCODE = "zhangjian2003";//敏感权限密钥，必须保密

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.printf("请求编号%d解析成功：来自%s的更改用户等级请求\n",requestID, username);
        try {
            User user = User.fetchUser(username);
            if (user == null) {
                NetUtility.sendResponse(exchange, new ModifyUserLevelResponse("未找到用户：" + username),requestID);//一般情况下不会出现这种情况
            } else if (user.getLevel() < User.ADMIN) {//只有超级管理员才能变更用户等级
                NetUtility.sendResponse(exchange, new ModifyUserLevelResponse("权限不足！"),requestID);
            } else if (!key.equals(KEYCODE)) {
                NetUtility.sendResponse(exchange, new ModifyUserLevelResponse("敏感权限密钥错误！"),requestID);
            } else {
                Connection connection = DriverManager.getConnection(Entry.DB_URL, user.getDBUsername(), user.getDBPassword());
                PreparedStatement statement = connection.prepareStatement("UPDATE user SET level=? WHERE username=?");
                statement.setInt(1, new_level);
                statement.setString(2, to_modify);
                int result = statement.executeUpdate();
                if (result == 1) {
                    NetUtility.sendResponse(exchange, new ModifyUserLevelResponse("更改用户等级成功！"),requestID);
                } else {
                    NetUtility.sendResponse(exchange, new ModifyUserLevelResponse("更改用户等级失败！"),requestID);
                }
            }
        } catch (SQLException e) {
            NetUtility.sendResponse(exchange, new ModifyUserLevelResponse("未知错误：" + e.getMessage()),requestID);
            throw new RuntimeException(e);
        }
    }
}
