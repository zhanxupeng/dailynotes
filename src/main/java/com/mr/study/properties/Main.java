package com.mr.study.properties;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author zhanxp
 * @version 1.0 2019/5/20
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        InputStream in = ClassLoader.getSystemResourceAsStream("system.properties");
//        Reader reader = new InputStreamReader(in);
//        BufferedReader bufferedReader = new BufferedReader(reader);
//        System.out.println(bufferedReader.readLine());
        properties.load(new InputStreamReader(in));
        System.out.println(properties.getProperty("hello"));
        System.out.println(properties.getProperty("test"));
    }
}
