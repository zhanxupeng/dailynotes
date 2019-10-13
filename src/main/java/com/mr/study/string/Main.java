package com.mr.study.string;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanxp
 * @version 1.0  2019/9/22
 * @Description todo
 */
public class Main {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(2);
        list.add("guan");
        list.add("bao");
        String[] stringArray = new String[2];
        String[] array = list.toArray(stringArray);
        for(String a:stringArray){
            System.out.print(a+" ");
        }
        System.out.println();
        for(String a:array){
            System.out.print(a+" ");
        }
        System.out.println();
    }


}
