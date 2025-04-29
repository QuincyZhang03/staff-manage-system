package com.zjquincy.ncu.net.request;

public class IllegalRequestException extends RuntimeException {
    public IllegalRequestException(String message) {
        super("非法请求：" + message);
    }
}
