package com.zjquincy.ncu.access;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {
    public static String convert(String input) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("错误：SHA-256算法不可用");
            return null;
        }
        byte[] digested_bytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder builder = new StringBuilder();
        for (byte b : digested_bytes) {
            int corrected_byte = 128 + b;//将-128~127映射到0~255
            String hex = Integer.toHexString(corrected_byte);//转换为十六进制，0~ff
            if (hex.length() == 1) hex = '0' + hex; //十六进制如果只有一位，补零
            builder.append(hex);
        }
        return builder.toString();
    }
}
