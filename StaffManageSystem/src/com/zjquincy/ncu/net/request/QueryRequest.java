package com.zjquincy.ncu.net.request;

import com.google.gson.annotations.SerializedName;
import com.sun.net.httpserver.HttpExchange;
import com.zjquincy.ncu.access.User;
import com.zjquincy.ncu.data.Department;
import com.zjquincy.ncu.data.Staff;
import com.zjquincy.ncu.net.NetUtility;
import com.zjquincy.ncu.net.response.QueryResponse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import static com.zjquincy.ncu.Entry.DB_URL;

/*
 * 查询请求格式：
 * String request_type = "QUERY"
 * String username 用户名
 * */
public class QueryRequest extends AbstractRequest {

    @SerializedName("username")
    private String username;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.printf("请求编号%d解析成功：来自%s的查询请求\n",requestID, username);
        try {
            User user = User.fetchUser(username);
            if (user == null) {
                NetUtility.sendResponse(exchange, new QueryResponse(false),requestID);
            } else {
                Connection connection = DriverManager.getConnection(DB_URL, user.getDBUsername(), user.getDBPassword());
                Statement statement = connection.createStatement();
                ResultSet staffResult = statement.executeQuery("SELECT * FROM staff");
                ArrayList<Staff> staffList = new ArrayList<>();
                while (staffResult.next()) {
                    Staff staff = new Staff(staffResult.getInt("id"), staffResult.getString("name"),
                            staffResult.getString("gender"), staffResult.getDate("birthday"),
                            staffResult.getString("telephone"), staffResult.getInt("department"),
                            staffResult.getString("position"), staffResult.getDouble("salary"));
                    staffList.add(staff);
                }
                ResultSet departmentResult = statement.executeQuery("SELECT * FROM department");
                ArrayList<Department> departmentList = new ArrayList<>();
                while (departmentResult.next()) {
                    Department department = new Department(departmentResult.getInt("id"),
                            departmentResult.getString("name"));
                    departmentList.add(department);
                }
                QueryResponse response;
                if (user.getLevel() < User.OPERATOR) {//普通用户，不予查询用户列表，直接返回现有数据
                    response = new QueryResponse(staffList, departmentList, null);
                } else {//管理员或超级管理员，继续查询用户列表
                    ResultSet userResult = statement.executeQuery("SELECT * FROM user");
                    ArrayList<User> userList = new ArrayList<>();
                    while (userResult.next()) {
                        User userData = new User(userResult.getString("username"),
                                userResult.getInt("level"), userResult.getTimestamp("timestamp"),
                                userResult.getString("password"));
                        userList.add(userData);
                    }
                    response = new QueryResponse(staffList, departmentList, userList);
                }
                NetUtility.sendResponse(exchange, response,requestID);
                statement.close();
                connection.close();
            }
        } catch (SQLException e) {
            NetUtility.sendResponse(exchange, new QueryResponse(false),requestID);
            throw new RuntimeException("服务器端执行数据库操作出现异常：" + e.getMessage());
        }
    }
}
