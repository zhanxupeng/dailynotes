package com.mr.xue.map;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author zhanxp
 * @version 1.0  2019/8/18
 * @Description todo
 */
public class Main {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("a", "这是第一个a");
        map.put("b", "这是第一个b");

        System.out.println("map中有几个值：" + map.size());
        System.out.println("map是否为空的：" + map.isEmpty());
        System.out.println("map中是否存在key为a的记录：" + map.containsKey("a"));
        System.out.println("map中是否存在value为'这是第一个a'的记录：" + map.containsValue("这是第一个a"));
        System.out.println("删除key为a的记录，并返回删除结果：" + map.remove("a"));
        //清空map
        map.clear();
        //等等，还有一些其他方法
    }
}
