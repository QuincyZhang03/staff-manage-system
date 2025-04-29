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

    public User(String username, int level) {
        this.username = username;
        this.level = level;
    }

    public static User fetchUser(String username) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, VISITOR_USERNAME, VISITOR_PASSWORD);
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery(String.format("SELECT * FROM user WHERE username=\"%s\"", username));
        if (!results.next()) {
            return null;//无此用户
        }
        int level = results.getInt("level");
        return new User(username, level);
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
    public String getDBPassword(){
        if (level == DEFAULT_USER)
            return DEFAULT_PASSWORD;
        else if (level == OPERATOR)
            return OPERATOR_PASSWORD;
        else if (level == ADMIN)
            return ADMIN_PASSWORD;
        return "unknown";
    }
}
