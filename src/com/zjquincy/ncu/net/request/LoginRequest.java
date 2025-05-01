package com.zjquincy.ncu.net.request;

import com.google.gson.annotations.SerializedName;
import com.zjquincy.ncu.access.SHA256;
import java.sql.Timestamp;

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

    public String getUsername() {
        return username;
    }

    public String getEncryptedPassword(Timestamp timestamp) {
        return SHA256.convert(String.format("%s@%s@%s", username, timestamp, input_password));
        //按照“用户名@时间戳@密码明文”的格式，以SHA256方式加密
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