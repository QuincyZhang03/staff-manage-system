package com.zjquincy.ncu.access;

import java.sql.*;

import static com.zjquincy.ncu.Entry.*;

public class PasswordVerifier {
    public static class Result {
        private final ResultType type;
        private final User user;
        private final Timestamp timestamp;

        public Result(ResultType type, User user, Timestamp timestamp) {
            this.type = type;
            this.user = user;
            this.timestamp = timestamp;
        }

        public ResultType getType() {
            return type;
        }

        public User getUser() {
            return user;
        }

        public Timestamp getTimestamp() {
            return timestamp;
        }
    }

    public enum ResultType {
        CORRECT,
        WRONG,
        NO_SUCH_USER
    }


    public static String getEncryptedPassword(String username, Timestamp timestamp, String input_password) {
        return SHA256.convert(String.format("%s@%s@%s", username, timestamp, input_password));
        //按照“用户名@时间戳@密码明文”的格式，以SHA256方式加密
    }

    public static Result verifyPassword(String username, String input_password) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, VISITOR_USERNAME, VISITOR_PASSWORD);
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery(String.format("SELECT * FROM user WHERE username=\"%s\"", username));
        if (!results.next()) {//无此用户
            return new Result(ResultType.NO_SUCH_USER, null, null);
        }
        //用户名存在，则现场对用户输入信息按照“用户名@时间戳@密码明文”的格式使用SHA-256加密，比对结果
        Timestamp timestamp = results.getTimestamp("timestamp");
        String correct_password = results.getString("password");
        String encrypted_password = getEncryptedPassword(username, timestamp, input_password);
        if (encrypted_password.equalsIgnoreCase(correct_password)) {//密码正确，登录成功
            User user = new User(username, results.getInt("level"));
            statement.close();
            connection.close();
            return new Result(ResultType.CORRECT, user, timestamp);
        }
        statement.close();
        connection.close();
        return new Result(ResultType.WRONG, null, null);//密码错误
    }
}