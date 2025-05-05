package com.zjquincy.ncu.net.request;

import com.google.gson.annotations.SerializedName;
import com.sun.net.httpserver.HttpExchange;
import com.zjquincy.ncu.Entry;
import com.zjquincy.ncu.access.User;
import com.zjquincy.ncu.net.NetUtility;
import com.zjquincy.ncu.net.response.DeleteUserResponse;
import java.io.IOException;
import java.sql.*;

/*
 * 删除用户请求格式：
 * String request_type = "DELETE_USER"
 * String username 用户名
 * String to_delete 要被删除的用户名
 */
public class DeleteUserRequest extends AbstractRequest {
    @SerializedName("username")
    private String username;
    @SerializedName("to_delete")
    private String to_delete;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.printf("请求编号%d解析成功：来自%s的删除用户请求\n",requestID, username);
        try {
            User user = User.fetchUser(username);
            User deletedUser = User.fetchUser(to_delete);
            if (user == null) {
                NetUtility.sendResponse(exchange, new DeleteUserResponse("未找到用户：" + username),requestID);//一般情况下不会出现这种情况
            } else if (deletedUser == null) {
                NetUtility.sendResponse(exchange, new DeleteUserResponse("未找到用户：" + to_delete),requestID);
            } else if (user.getLevel() < User.OPERATOR) {//用户权限不足，不能删人
                NetUtility.sendResponse(exchange, new DeleteUserResponse("权限不足！"),requestID);
            } else if (user.getLevel() <= deletedUser.getLevel()) {//只能上删下，不能下删上
                NetUtility.sendResponse(exchange, new DeleteUserResponse(
                        String.format("%s(等级%d)的权限等级不低于您，不能删除它的信息", to_delete, deletedUser.getLevel())),requestID);
            } else {
                Connection connection = DriverManager.getConnection(Entry.DB_URL, user.getDBUsername(), user.getDBPassword());
                PreparedStatement statement = connection.prepareStatement("DELETE FROM visitor WHERE BINARY username=?");
                statement.setString(1, to_delete);
                int result = statement.executeUpdate();
                if (result == 1) {
                    NetUtility.sendResponse(exchange, new DeleteUserResponse("删除成功！"),requestID);
                } else {
                    NetUtility.sendResponse(exchange, new DeleteUserResponse("删除失败！"),requestID);
                }
                statement.close();
                connection.close();
            }
        } catch (SQLException e) {
            NetUtility.sendResponse(exchange, new DeleteUserResponse("未知错误：" + e.getMessage()),requestID);
            throw new RuntimeException(e);
        }
    }
}
