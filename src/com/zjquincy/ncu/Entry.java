package com.zjquincy.ncu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.zjquincy.ncu.blockchain.BlockChain;
import com.zjquincy.ncu.blockchain.BlockChainUtility;
import com.zjquincy.ncu.net.request.AbstractRequest;
import com.zjquincy.ncu.net.request.IllegalRequestException;
import com.zjquincy.ncu.net.NetUtility;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.zjquincy.ncu.Entry.gson;

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
    public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();//指定日期格式，以规定格式序列化
    public static BlockChain blockChain;

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
    private static final SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String json_request = NetUtility.readRequest(exchange);//读取发送过来的json
        JsonObject json_type = JsonParser.parseString(json_request).getAsJsonObject();
        String request_type_raw = json_type.get("request_type").getAsString();//获取请求类型
        Class<? extends AbstractRequest> request_type = AbstractRequest.convertType(request_type_raw);
        //将请求类型字符串转换为Class类对象，用于反序列化
        if (request_type == null) {
            throw new IllegalRequestException("未知的请求类型：" + request_type_raw);
        } else {
            System.out.printf("[消息 %s]接收到请求，类型为：%s\n", dateFormat.format(new Date()), request_type.getSimpleName());
            AbstractRequest request = gson.fromJson(json_request, request_type);
            //把Json反序列化为请求对象，具体的处理逻辑位于各自类的handle()方法里，这是多态的使用。
            request.handle(exchange);
        }
    }
}
