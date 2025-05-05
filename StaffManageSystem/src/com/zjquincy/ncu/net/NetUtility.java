package com.zjquincy.ncu.net;

import com.sun.net.httpserver.HttpExchange;
import com.zjquincy.ncu.net.response.AbstractResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static com.zjquincy.ncu.Entry.gson;

public class NetUtility {
    public static String readRequest(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody(); //接受请求主体
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    public static void sendResponse(HttpExchange exchange, AbstractResponse response, int requestID) throws IOException {//发送消息后会关闭流
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");//通用的，响应头
        String json_response = gson.toJson(response);
        exchange.sendResponseHeaders(200, json_response.getBytes(StandardCharsets.UTF_8).length);
        //200表示OK，写入长度是字节数，不是字符数
        OutputStream os = exchange.getResponseBody();
        os.write(json_response.getBytes(StandardCharsets.UTF_8));
        System.out.printf("%d号请求响应成功：%s\n", requestID, response.getLogMessage());
        os.close();
    }
}
