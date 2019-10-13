package com.mr.study.base64;


import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author zhanxp
 * @version 1.0 2019/7/29
 */
public class Main {
    private static void baseTest(Base64.Encoder encoder, Base64.Decoder decoder, String testValue) {
        System.out.println("当前值为:" + testValue);
        byte[] bytes = testValue.getBytes(StandardCharsets.UTF_8);

        String encoderString = encoder.encodeToString(bytes);
        System.out.println("编码后的值为：" + encoderString);

        bytes = decoder.decode(encoderString);
        String decoderString = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("解码后的值为：" + decoderString);
    }

    public static void main(String[] args) throws Exception {
        baseTest(Base64.getEncoder(), Base64.getDecoder(), "测试");

        baseTest(Base64.getUrlEncoder(), Base64.getUrlDecoder(), "测试");

        baseTest(Base64.getMimeEncoder(), Base64.getMimeDecoder(), "测试");
    }
}
