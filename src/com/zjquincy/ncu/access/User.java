package com.zjquincy.ncu.access;

import com.google.gson.annotations.SerializedName;

import java.sql.*;

import static com.zjquincy.ncu.Entry.*;

public class User {
    public static final int DEFAULT_USER = 0;
    public static final int OPERATOR = 1;
    public static final int ADMIN = 2;

    @SerializedName("username")
    private String username;
    @SerializedName("level")
    private int level;

    private final transient Timestamp timestamp; //transient字段会被gson忽略，不会被发送给客户端

    public User(String username, int level, Timestamp timestamp) {
        this.username = username;
        this.level = level;
        this.timestamp = timestamp;
    }

    public static User fetchUser(String username) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, VISITOR_USERNAME, VISITOR_PASSWORD);
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery(String.format("SELECT * FROM user WHERE username=\"%s\"", username));
        if (!results.next()) {
            return null;//无此用户
        }
        int level = results.getInt("level");
        Timestamp timestamp = results.getTimestamp("timestamp");
        statement.close();
        connection.close();
        return new User(username, level, timestamp);
    }

    public String getDBUsername() {
        if (level == DEFAULT_USER)
            return DEFAULT_USERNAME;
        else if (level == OPERATOR)
            return OPERATOR_USERNAME;
        else if (level == ADMIN)
            return ADMIN_USERNAME;
        return "unknown";
    }

    public String getDBPassword() {
        if (level == DEFAULT_USER)
            return DEFAULT_PASSWORD;
        else if (level == OPERATOR)
            return OPERATOR_PASSWORD;
        else if (level == ADMIN)
            return ADMIN_PASSWORD;
        return "unknown";
    }

    public int getLevel() {
        return level;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
