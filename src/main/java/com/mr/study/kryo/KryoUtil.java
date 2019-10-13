package com.mr.study.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @author zhanxp
 * @version 1.0 2019/7/5
 */
public class KryoUtil {
    private static final String DEFAULT_ENCODING = "UTF-8";
    /**
     * 每个线程的kryo实例
     */
    private static final ThreadLocal<Kryo> kryoLocal = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            //支持对象循环引用（否则会栈溢出) 默认值就是true
            kryo.setReferences(true);
            //不强行要求注册类（注册行为无法保证多个jvm内同一个类的注册编号相同，而且业务系统中大量的Class也难以一一注册）默认false
            kryo.setRegistrationRequired(false);
            //Fix the NPE bug when deserializing Collections.
            ((Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy())
                    .setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());
            return kryo;

        }
    };

    /**
     * 获取当前线程的信息
     *
     * @return
     */
    public static Kryo getInstance() {
        return kryoLocal.get();
    }

    /**
     * 将对象【及类型】序列化为字节数组
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> byte[] writeToByteArray(T obj) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        Kryo kryo = getInstance();
        kryo.writeClassAndObject(output, obj);
        output.flush();
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 将对象[及类型]序列化为String
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String writeToString(T obj) {
        return Base64.getEncoder().encodeToString(writeToByteArray(obj));
    }

    /**
     * 将字节数组反序列化为原对象
     *
     * @param byteArray
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T readFromByteArray(byte[] byteArray) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        Input input = new Input(byteArrayInputStream);
        Kryo kryo = getInstance();
        return (T) kryo.readClassAndObject(input);
    }

    /**
     * 将String反序列化为原对象
     *
     * @param str
     * @param <T>
     * @return
     */
    public static <T> T readFromString(String str) {
        return readFromByteArray(Base64.getDecoder().decode(str));
    }
// 
//            //-----------------------------------------------
//            //          只序列化/反序列化对象
//            //          序列化的结果里，不包含类型的信息
//            //-----------------------------------------------
//         
//            /**
//      * 将对象序列化为字节数组
//      *
//      * @param obj 任意对象
//      * @param <T> 对象的类型
//      * @return 序列化后的字节数组
//      */
//            public static <T> byte[] writeObjectToByteArray(T obj) {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        Output output = new Output(byteArrayOutputStream);
// 
//        Kryo kryo = getInstance();
//        kryo.writeObject(output, obj);
//        output.flush();
// 
//        return byteArrayOutputStream.toByteArray();
//    }
// 
//            /**
//      * 将对象序列化为 String
//      * 利用了 Base64 编码
//      *
//      * @param obj 任意对象
//      * @param <T> 对象的类型
//      * @return 序列化后的字符串
//      */
//            public static <T> String writeObjectToString(T obj) {
//        try {
//            return new String(Base64.encodeBase64(writeObjectToByteArray(obj)), DEFAULT_ENCODING);
//        } catch (UnsupportedEncodingException e) {
//            throw new IllegalStateException(e);
//        }
//    }
// 
//            /**
//      * 将字节数组反序列化为原对象
//      *
//      * @param byteArray writeToByteArray 方法序列化后的字节数组
//      * @param clazz     原对象的 Class
//      * @param <T>       原对象的类型
//      * @return 原对象
//      */
//            @SuppressWarnings("unchecked")
//    public static <T> T readObjectFromByteArray(byte[] byteArray, Class<T> clazz) {
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
//        Input input = new Input(byteArrayInputStream);
// 
//        Kryo kryo = getInstance();
//        return kryo.readObject(input, clazz);
//    }
// 
//            /**
//      * 将 String 反序列化为原对象
//      * 利用了 Base64 编码
//      *
//      * @param str   writeToString 方法序列化后的字符串
//      * @param clazz 原对象的 Class
//      * @param <T>   原对象的类型
//      * @return 原对象
//      */
//            public static <T> T readObjectFromString(String str, Class<T> clazz) {
//        try {
//            return readObjectFromByteArray(Base64.decodeBase64(str.getBytes(DEFAULT_ENCODING)), clazz);
//        } catch (UnsupportedEncodingException e) {
//            throw new IllegalStateException(e);
//        }
//    }
}
