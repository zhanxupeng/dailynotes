package com.mr.xue.uuid;

import java.util.UUID;

/**
 * @author zhanxp
 * @version 1.0  2019/9/8
 * @Description todo
 */
public class Main {
    public static void main(String[] args) {
        String uuid = UUID.randomUUID().toString();
        System.out.println("原始uuid:" + uuid);

        //删掉-
        System.out.println("删除-后的值:" + uuid.replace("-", ""));

        //删掉-,并且转大写
        System.out.println("删除-后转大写的值：" + uuid.replace("-", "").toUpperCase());
    }
}
