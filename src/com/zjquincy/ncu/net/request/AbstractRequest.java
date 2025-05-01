package com.zjquincy.ncu.net.request;

public abstract class AbstractRequest {
    public enum RequestType {
        LOGIN,
        REGISTER,
        QUERY,
        UNKNOWN
    }

    public static RequestType convertType(String raw) {
        try {
            return RequestType.valueOf(raw);
        } catch (IllegalArgumentException e) {
            return RequestType.UNKNOWN;
        }
    }
}