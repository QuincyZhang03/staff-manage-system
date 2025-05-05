package com.zjquincy.ncu.net.request;

import com.google.gson.annotations.SerializedName;
import com.sun.net.httpserver.HttpExchange;
import com.zjquincy.ncu.access.SHA256;
import com.zjquincy.ncu.net.NetUtility;
import com.zjquincy.ncu.net.response.RegisterResponse;
import java.io.IOException;
import java.sql.*;

import static com.zjquincy.ncu.Entry.*;

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

    public String getEncryptedPassword(Timestamp timestamp) {
        return SHA256.convert(String.format("%s@%s@%s", username, timestamp, input_password));
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.printf("请求编号%d解析成功：来自%s的注册请求\n",requestID, username);
        try {
            Connection connection = DriverManager.getConnection(DB_URL, VISITOR_USERNAME, VISITOR_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(String.format("SELECT * FROM user WHERE username=\"%s\"", username));
            if (!results.next()) {//无此用户，可以注册
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                timestamp.setNanos(0);//注册时间戳这里非常重要：MySQL里不能存小数点后面的，一定要删掉
                String encrypted_password = getEncryptedPassword(timestamp);
                PreparedStatement sql = connection.prepareStatement("INSERT INTO visitor VALUES(?,?,?)");
                //PreparedStatement填充数据模板，可以用来填充不方便用文本表示的数据，索引从1开始
                //visitor视图的结构是(用户名,时间戳,密码)，防止用户修改等级
                //public用户对user表只有SELECT权限，而对visitor视图还有INSERT权限
                sql.setString(1, username);
                sql.setTimestamp(2, timestamp);
                sql.setString(3, encrypted_password);
                int result = sql.executeUpdate();
                if (result == 1) {//注册成功
                    NetUtility.sendResponse(exchange, new RegisterResponse(RegisterResponse.Result.SUCCESS),requestID);
                } else {
                    NetUtility.sendResponse(exchange, new RegisterResponse(RegisterResponse.Result.ERROR),requestID);
                }
                sql.close();
            } else {//用户名已存在，不予注册
                NetUtility.sendResponse(exchange, new RegisterResponse(RegisterResponse.Result.USER_ALREADY_EXISTS),requestID);
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            NetUtility.sendResponse(exchange, new RegisterResponse(RegisterResponse.Result.ERROR),requestID);
            throw new RuntimeException("服务器端执行数据库操作出现异常：" + e.getMessage());
        }
    }
}
