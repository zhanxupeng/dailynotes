package com.mr.study.stringbuilder;

/**
 * @author zhanxp
 * @version 1.0  2019/9/1
 * @Description todo
 */
public class Main {
    public static void main(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            stringBuilder.append("测试").append(i);
        }
        System.out.println("当前字符串为:" + stringBuilder.toString());
    }
}
