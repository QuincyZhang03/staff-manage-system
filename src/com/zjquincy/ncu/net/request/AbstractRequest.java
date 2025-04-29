package com.zjquincy.ncu.net.request;

public abstract class AbstractRequest {
    public enum RequestType {
        LOGIN,
        REGISTER,
        QUERY,
        UNKNOWN
    }

    public static RequestType convertType(String raw) {
        if (raw.equalsIgnoreCase("login"))
            return RequestType.LOGIN;
        if (raw.equalsIgnoreCase("register"))
            return RequestType.REGISTER;
        if (raw.equalsIgnoreCase("query"))
            return RequestType.QUERY;
        return RequestType.UNKNOWN;
    }
}