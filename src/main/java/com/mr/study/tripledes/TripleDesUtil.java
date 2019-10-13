package com.mr.study.tripledes;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

/**
 * @author zhanxp
 * @version 1.0 2019/8/7
 */
public class TripleDesUtil {
    private final static String CBC = "DESede/CBC/PKCS5Padding";
    private final static String TRIPLE_DES = "TripleDES";
    private final static Charset CHARSET = StandardCharsets.UTF_8;
    private final static int SECRET_LENGTH = 168;

    //-----------------------------测试---------------------------

    public static void main(String[] args) throws Exception {
        //生成密钥
        String key = getKey();
        //生成偏移，随便写，不超过8位
        String sourceIv = "12345678";
        String iv = Base64.getEncoder().encodeToString(sourceIv.getBytes(CHARSET));
        System.out.println("当前的key为:" + key);
        System.out.println("当前的iv为:" + iv);

        String encrypt = encrypt("占旭鹏", key, iv);
        System.out.println("加密后的结果为:" + encrypt);
        System.out.println("解密后的结果为:" + decrypt(encrypt, key, iv));
    }
    //-----------------------------测试结束----------------------

    public static String getKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(TRIPLE_DES);
        keyGenerator.init(SECRET_LENGTH);
        Key key = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static String encrypt(String src, String key, String iv) throws Exception {
        byte[] sourceBytes = src.getBytes(CHARSET);
        byte[] encryptBytes = operate(sourceBytes, Cipher.ENCRYPT_MODE, key, iv);
        return Base64.getEncoder().encodeToString(encryptBytes);
    }

    public static String decrypt(String src, String key, String iv) throws Exception {
        byte[] sourceBytes = Base64.getDecoder().decode(src);
        byte[] encryptBytes = operate(sourceBytes, Cipher.DECRYPT_MODE, key, iv);
        return new String(encryptBytes, CHARSET);
    }

    private static byte[] operate(byte[] source, int mode, String key, String iv) throws Exception {
        Cipher cipher = Cipher.getInstance(CBC);
        SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(key.getBytes(CHARSET)), TRIPLE_DES);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(Base64.getDecoder().decode(iv.getBytes(CHARSET)));
        cipher.init(mode, keySpec, ivParameterSpec);
        return cipher.doFinal(source);
    }
}
