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
            case "CHECK_INTEGRITY" -> CheckIntegrityRequest.class;
            case "CREATE" -> CreateRequest.class;
            case "DELETE" -> DeleteRequest.class;
            case "UPDATE" -> UpdateRequest.class;
            case "DELETE_USER" -> DeleteUserRequest.class;
            case "RESET_USER" -> ResetUserRequest.class;
            case "MODIFY_USER_LEVEL" -> ModifyUserLevelRequest.class;
            default -> null;
        };
    }

    public abstract void handle(HttpExchange exchange) throws IOException;//具体处理方法的实现在各个子类里
}