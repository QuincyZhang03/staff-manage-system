package com.zjquincy.ncu.net.request;

import com.google.gson.annotations.SerializedName;
import com.sun.net.httpserver.HttpExchange;
import com.zjquincy.ncu.access.PasswordVerifier;
import com.zjquincy.ncu.access.User;
import com.zjquincy.ncu.net.NetUtility;
import com.zjquincy.ncu.net.response.LoginResponse;
import java.io.IOException;
import java.sql.SQLException;

/*
 * 登录请求Json格式：
 * String request_type = "LOGIN"
 * String username 用户名
 * String input_password 输入的密码
 * */
public class LoginRequest extends AbstractRequest {
    @SerializedName("username") //用@SerializedName标识的属性可以被Gson序列化和反序列化
    private String username;
    @SerializedName("input_password")
    private String input_password;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            PasswordVerifier.Result result = PasswordVerifier.verifyPassword(username, input_password);
            if (result.getType() == PasswordVerifier.ResultType.NO_SUCH_USER) {//无此用户
                NetUtility.sendResponse(exchange, new LoginResponse(LoginResponse.Result.NO_SUCH_USER));
            } else {//用户名存在，则现场对用户输入信息按照“用户名@时间戳@密码明文”的格式使用SHA-256加密，比对结果
                if (result.getType()== PasswordVerifier.ResultType.CORRECT) {//密码正确，登录成功
                    User user = result.getUser();
                    NetUtility.sendResponse(exchange, new LoginResponse(user)); //把用户信息一并返回给客户端
                } else {//密码错误
                    NetUtility.sendResponse(exchange, new LoginResponse(LoginResponse.Result.WRONG_PASSWORD));
                }
            }
        } catch (SQLException e) {
            NetUtility.sendResponse(exchange, new LoginResponse(LoginResponse.Result.ERROR));
            throw new RuntimeException("服务器端执行数据库操作出现异常：" + e.getMessage());

        }
    }
}

/*
以下是添加超级管理员账号的代码，可用于调试
    public static void main(String[] args) throws SQLException {
        Timestamp timestamp=new Timestamp(System.currentTimeMillis());
        timestamp.setNanos(0);
        LoginRequest loginRequest=new LoginRequest();
        loginRequest.username="quincyzhang";
        loginRequest.input_password="Zhangjian5e";
        Connection connection = DriverManager.getConnection(Entry.DB_URL, VISITOR_USERNAME, VISITOR_PASSWORD);
        Statement statement = connection.createStatement();
        statement.execute("USE staff");
        String sql="INSERT INTO user VALUES(?,?,?,?)";
        PreparedStatement preparedStatement=connection.prepareStatement(sql);
        preparedStatement.setString(1, loginRequest.username);
        preparedStatement.setTimestamp(2,timestamp);
        preparedStatement.setString(3,loginRequest.getEncryptedPassword(timestamp));
        preparedStatement.setInt(4,2);
        System.out.println(preparedStatement.executeUpdate());
        preparedStatement.close();
        connection.close();
    }
   */