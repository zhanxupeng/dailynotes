package com.mr.study.rsa;


import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.Base64;

/**
 * @author zhanxp
 * @version 1.0 2019/8/6
 */
public class RsaUtil {
    private static final String RSA = "RSA";
    private final static String MD5_RSA = "MD5withRSA";
    private static int SECRET_LENGTH = 512;
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = SECRET_LENGTH / 8;

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = MAX_DECRYPT_BLOCK - 1;


    //-------------------测试用-------------------------
    private static final BigInteger MODULES = new BigInteger("8623627415824249740153721843687908164006147662483993437887790209065022399215029202804267148689732265965650454491167977271321187259638928328935661383986077");
    private static final BigInteger PUBLIC_EXPONENT = new BigInteger("65537");
    private static final BigInteger PRIVATE_EXPONENT = new BigInteger("6149319926510439341236307473962296294131548571201984609194960096437671724102526267398675041437251868499630084003846689856316531322150929513416288493823233");

    public static void main(String[] args) throws Exception {
        String encrypt = publicEncrypt("占旭鹏", MODULES, PUBLIC_EXPONENT);
        System.out.println(encrypt);
        System.out.println(privateDecrypt(encrypt, MODULES, PRIVATE_EXPONENT));

        String sign = privateSign("占旭鹏", MODULES, PRIVATE_EXPONENT);
        System.out.println(sign);
        System.out.println(publicVerify("占旭鹏", MODULES, PUBLIC_EXPONENT, sign));
    }
    //---------------以上测试用-----------------------


    /**
     * 公钥加密，并对结果BASE64
     *
     * @param data
     * @param modules
     * @param exponent
     * @return
     * @throws Exception
     */
    public static String publicEncrypt(String data, BigInteger modules, BigInteger exponent) throws Exception {
        return encrypt(getPublicKey(modules, exponent), data);
    }

    /**
     * BASE64解码，并私钥解密
     *
     * @param data
     * @param modules
     * @param exponent
     * @return
     * @throws Exception
     */
    public static String privateDecrypt(String data, BigInteger modules, BigInteger exponent) throws Exception {
        return decrypt(getPrivateKey(modules, exponent), data);
    }

    /**
     * 私钥签名，并对结果BASE64
     *
     * @param data
     * @param modules
     * @param exponent
     * @return
     * @throws Exception
     */
    public static String privateSign(String data, BigInteger modules, BigInteger exponent) throws Exception {
        PrivateKey key = getPrivateKey(modules, exponent);
        Signature signature = Signature.getInstance(MD5_RSA);
        signature.initSign(key);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    /**
     * 公钥验签
     *
     * @param data
     * @param modules
     * @param exponent
     * @param sign
     * @return
     * @throws Exception
     */
    public static boolean publicVerify(String data, BigInteger modules, BigInteger exponent, String sign) throws Exception {
        PublicKey key = getPublicKey(modules, exponent);
        Signature signature = Signature.getInstance(MD5_RSA);
        signature.initVerify(key);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        return signature.verify(Base64.getDecoder().decode(sign));
    }

    /**
     * 生成公钥和私钥
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static KeyDTO getSecretKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
        keyPairGenerator.initialize(SECRET_LENGTH);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] publicKeyData = publicKey.getEncoded();
        byte[] privateKeyData = privateKey.getEncoded();
        return new KeyDTO(publicKeyData, privateKeyData);
    }

    /**
     * 公钥加密，并对结果BASE64
     *
     * @param publicKey
     * @param data
     * @return
     * @throws Exception
     */
    private static String encrypt(PublicKey publicKey, String data) throws Exception {
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptByte = rsaSplitCodec(cipher, MAX_ENCRYPT_BLOCK, bytes);
        return Base64.getEncoder().encodeToString(encryptByte);
    }

    /**
     * BASE64解码，并私钥解密
     *
     * @param privateKey
     * @param data
     * @return
     * @throws Exception
     */
    private static String decrypt(PrivateKey privateKey, String data) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(data);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptByte = rsaSplitCodec(cipher, MAX_DECRYPT_BLOCK, bytes);
        return new String(decryptByte, StandardCharsets.UTF_8);
    }

    private static byte[] rsaSplitCodec(Cipher cipher, int maxBlock, byte[] datas) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try {
            while (datas.length > offSet) {
                if (datas.length - offSet > maxBlock) {
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        } catch (Exception e) {
            throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
        }
        byte[] resultDatas = out.toByteArray();
        out.close();
        return resultDatas;
    }

    private static PublicKey getPublicKey(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePublic(keySpec);
    }

    private static PrivateKey getPrivateKey(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePrivate(keySpec);
    }

    private static void printPublicKeyInfo(PublicKey key) {
        RSAPublicKey publicKey = (RSAPublicKey) key;
        System.out.println("exponent:" + publicKey.getPublicExponent());
        System.out.println("modules:" + publicKey.getModulus());
    }

    private static void printPrivateKeyInfo(PrivateKey key) {
        RSAPrivateKey privateKey = (RSAPrivateKey) key;
        System.out.println("exponent:" + privateKey.getPrivateExponent());
        System.out.println("modules:" + privateKey.getModulus());
    }

    private static PublicKey getPublicKey(BigInteger modules, BigInteger publicExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modules, publicExponent);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePublic(keySpec);
    }

    private static PrivateKey getPrivateKey(BigInteger modules, BigInteger privateExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modules, privateExponent);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePrivate(keySpec);
    }

    public static class KeyDTO {
        private byte[] publicKey;
        private byte[] privateKey;

        public KeyDTO(byte[] publicKey, byte[] privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }

        public byte[] getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(byte[] publicKey) {
            this.publicKey = publicKey;
        }

        public byte[] getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(byte[] privateKey) {
            this.privateKey = privateKey;
        }
    }
}
