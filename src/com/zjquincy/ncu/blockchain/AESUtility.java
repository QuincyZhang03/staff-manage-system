package com.zjquincy.ncu.blockchain;

import com.zjquincy.ncu.Entry;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class AESUtility {//AESUtility类封装了AES算法，对字节数组进行加密解密
    public static final String ALGORITHM = "AES";//选用AES对称加密

    public static final Cipher cipher; //加密算法是通用的，在静态构造方法里初始化即可
    public static final SecretKeySpec key = new SecretKeySpec(Entry.ENCRYPT_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);


    static {
        try {
            cipher = Cipher.getInstance(ALGORITHM);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //加密密钥，必须保密，该秘钥可以用于对.bin文件进行解密
    public static byte[] encrypt(byte[] data) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decrypt(byte[] encryptedData) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
