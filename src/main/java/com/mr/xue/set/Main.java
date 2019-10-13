package com.mr.xue.set;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author zhanxp
 * @version 1.0  2019/9/8
 * @Description todo
 */
public class Main {
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
        set.add("aaa");
        set.add("bbb");
        set.add("ccc");
        set.add("aaa");

        System.out.println("set中是否包含aaa：" + set.contains("aaa"));
        System.out.println("set是否为空：" + set.isEmpty());

        //遍历方式1
        System.out.print("set中的数据为：");
        for (String s : set) {
            System.out.print(s + "  ");
        }
        System.out.println();
        //遍历方式2
        Iterator<String> iterator = set.iterator();
        System.out.print("set中的数据为：");
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + "  ");
        }
        System.out.println();

        set = new TreeSet<>();
        set.add("aaa");
        set.add("bbb");
        set.add("ccc");
        set.add("aaa");

        //删除bbb
        boolean result = set.remove("bbb");
        System.out.println("是否删除成功：" + result);

        //遍历方式1
        System.out.print("set中的数据为：");
        for (String s : set) {
            System.out.print(s + "  ");
        }
        System.out.println();
        //遍历方式2
        iterator = set.iterator();
        System.out.print("set中的数据为：");
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + "  ");
        }
        System.out.println();
    }
}
