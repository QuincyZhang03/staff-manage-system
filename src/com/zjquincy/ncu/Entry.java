package com.zjquincy.ncu;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.zjquincy.ncu.access.User;
import com.zjquincy.ncu.blockchain.BlockChain;
import com.zjquincy.ncu.blockchain.BlockChainUtility;
import com.zjquincy.ncu.data.Department;
import com.zjquincy.ncu.data.Staff;
import com.zjquincy.ncu.net.request.*;
import com.zjquincy.ncu.net.response.AbstractResponse;
import com.zjquincy.ncu.net.response.LoginResponse;
import com.zjquincy.ncu.net.response.QueryResponse;
import com.zjquincy.ncu.net.response.RegisterResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;

import static com.zjquincy.ncu.Entry.*;


//TODO:普通管理员增删改请求、超级管理员更改用户等级的请求的处理和上链，以及对超级管理员验证区块链完整性请求的处理
public class Entry {
    private static final int PORT = 23456;
    public static final String DB_URL = "jdbc:mysql://localhost:3306/staff";
    public static final String BLOCKCHAIN_DIR = "history";
    public static final String ENCRYPT_KEY = "zjQuincy20030506";//AES加密密钥必须是16字节、24字节或32字节。必须保密。
    public static final String VISITOR_USERNAME = "public";//公开账号，用于核验身份
    public static final String VISITOR_PASSWORD = "123456";
    public static final String DEFAULT_USERNAME = "default";//普通用户
    public static final String DEFAULT_PASSWORD = "123456";
    public static final String OPERATOR_USERNAME = "operator";//管理员
    public static final String OPERATOR_PASSWORD = "123456";
    public static final String ADMIN_USERNAME = "admin";//超级管理员
    public static final String ADMIN_PASSWORD = "123456";
    public static Gson gson = new Gson();
    private static BlockChain blockChain;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/api/staff", new ServerHandler()); //访问地址：localhost:23456/api/staff
        server.setExecutor(null);//使用默认线程池
        server.start();
        System.out.printf("管理系统服务器启动成功！开始监听于localhost:%d\n", PORT);
        blockChain = BlockChainUtility.readBlockChain(BLOCKCHAIN_DIR);
        System.out.println("区块链数据读取完成。");
    }
}

class ServerHandler implements HttpHandler { //服务器请求处理器
    private String readRequest(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody(); //接受请求主体
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    private void sendResponse(HttpExchange exchange, AbstractResponse response) throws IOException {//发送消息后会关闭流
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");//通用的，响应头
        String json_response = gson.toJson(response);
        exchange.sendResponseHeaders(200, json_response.getBytes(StandardCharsets.UTF_8).length);
        //200表示OK，写入长度是字节数，不是字符数
        OutputStream os = exchange.getResponseBody();
        os.write(json_response.getBytes(StandardCharsets.UTF_8));
        os.close();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String json_request = readRequest(exchange);//读取发送过来的json
        JsonObject json_type = JsonParser.parseString(json_request).getAsJsonObject();
        String request_type_raw = json_type.get("request_type").getAsString();//获取请求类型
        AbstractRequest.RequestType request_type = AbstractRequest.convertType(request_type_raw);
        if (request_type == AbstractRequest.RequestType.UNKNOWN) {
            throw new IllegalRequestException("未知的请求类型：" + request_type_raw);
        }
        if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {//处理客户端发来的POST请求
            if (request_type == AbstractRequest.RequestType.LOGIN) {//处理登录请求
                handleLogin(exchange, gson.fromJson(json_request, LoginRequest.class));//完整解析出一个LoginRequest对象来
            } else if (request_type == AbstractRequest.RequestType.REGISTER) {//处理注册请求
                handleRegister(exchange, gson.fromJson(json_request, RegisterRequest.class));
            }
        } else if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {//处理客户端发来的GET请求，一般是查询
            if (request_type == AbstractRequest.RequestType.QUERY) {//处理查询请求
                handleQuery(exchange, gson.fromJson(json_request, QueryRequest.class));
            }
        }
    }

    public void handleLogin(HttpExchange exchange, LoginRequest loginRequest) throws IOException {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, VISITOR_USERNAME, VISITOR_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(String.format("SELECT * FROM user WHERE username=\"%s\"", loginRequest.getUsername()));
            if (!results.next()) {//无此用户
                sendResponse(exchange, new LoginResponse(LoginResponse.Result.NO_SUCH_USER));
            } else {//用户名存在，则现场对用户输入信息按照“用户名@时间戳@密码明文”的格式使用SHA-256加密，比对结果
                Timestamp timestamp = results.getTimestamp("timestamp");
                String correct_password = results.getString("password");
                String encrypted_password = loginRequest.getEncryptedPassword(timestamp);
                if (encrypted_password.equalsIgnoreCase(correct_password)) {//密码正确，登录成功
                    User user = new User(loginRequest.getUsername(), results.getInt("level"));
                    sendResponse(exchange, new LoginResponse(user)); //把用户信息一并返回给客户端
                } else {//密码错误
                    sendResponse(exchange, new LoginResponse(LoginResponse.Result.WRONG_PASSWORD));
                }
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            sendResponse(exchange, new LoginResponse(LoginResponse.Result.ERROR));
            throw new RuntimeException("服务器端执行数据库操作出现异常：" + e.getMessage());
        }
    }

    public void handleRegister(HttpExchange exchange, RegisterRequest registerRequest) throws IOException {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, VISITOR_USERNAME, VISITOR_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(String.format("SELECT * FROM user WHERE username=\"%s\"", registerRequest.getUsername()));
            if (!results.next()) {//无此用户，可以注册
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                timestamp.setNanos(0);//注册时间戳这里非常重要：MySQL里不能存小数点后面的，一定要删掉
                String encrypted_password = registerRequest.getEncryptedPassword(timestamp);
                PreparedStatement sql = connection.prepareStatement("INSERT INTO visitor VALUES(?,?,?)");
                //PreparedStatement填充数据模板，可以用来填充不方便用文本表示的数据，索引从1开始
                //visitor视图的结构是(用户名,时间戳,密码)，防止用户修改等级
                //public用户对user表只有SELECT权限，而对visitor视图还有INSERT权限
                sql.setString(1, registerRequest.getUsername());
                sql.setTimestamp(2, timestamp);
                sql.setString(3, encrypted_password);
                int result = sql.executeUpdate();
                if (result == 1) {//注册成功
                    sendResponse(exchange, new RegisterResponse(RegisterResponse.Result.SUCCESS));
                } else {
                    sendResponse(exchange, new RegisterResponse(RegisterResponse.Result.ERROR));
                }
                sql.close();
            } else {//用户名已存在，不予注册
                sendResponse(exchange, new RegisterResponse(RegisterResponse.Result.USER_ALREADY_EXISTS));
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            sendResponse(exchange, new RegisterResponse(RegisterResponse.Result.ERROR));
            throw new RuntimeException("服务器端执行数据库操作出现异常：" + e.getMessage());
        }
    }

    public void handleQuery(HttpExchange exchange, QueryRequest queryRequest) throws IOException {
        try {
            User user = User.fetchUser(queryRequest.getUsername());
            if (user == null) {
                sendResponse(exchange, new QueryResponse(false));
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
                QueryResponse response = new QueryResponse(staffList, departmentList);
                sendResponse(exchange, response);
                statement.close();
                connection.close();
            }
        } catch (SQLException e) {
            sendResponse(exchange, new QueryResponse(false));
            throw new RuntimeException("服务器端执行数据库操作出现异常：" + e.getMessage());
        }
    }
}