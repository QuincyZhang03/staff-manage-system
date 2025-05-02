package com.zjquincy.ncu.net.request;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public abstract class AbstractRequest {

    public static Class<? extends AbstractRequest> convertType(String raw) {
        return switch (raw) {
            case "LOGIN" -> LoginRequest.class;
            case "REGISTER" -> RegisterRequest.class;
            case "QUERY" -> QueryRequest.class;
            case "CHANGE_PASSWORD" -> ChangePasswordRequest.class;
            default -> null;
        };
    }

    public abstract void handle(HttpExchange exchange) throws IOException;
}