package com.mr.xue.random;

import java.util.Random;

/**
 * @author zhanxp
 * @version 1.0  2019/9/8
 * @Description todo
 */
public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        System.out.println("nextInt()：" + random.nextInt());   //随机生成一个整数，这个整数的范围就是int类型的范围-2^31~2^31-1
        System.out.println("nextLong()：" + random.nextLong());      //随机生成long类型范围的整数
        System.out.println("nextFloat()：" + random.nextFloat());    //随机生成[0, 1.0)区间的小数
        System.out.println("nextDouble()：" + random.nextDouble());  //随机生成[0, 1.0)区间的小数

        System.out.println("[0,9]的一个正整数：" + random.nextInt(10));//随机成功[0,9])的一个整数

        //如果要随机生成[5,14]的正整数，那么就是[0,9]+5
        System.out.println("[5,14]的一个正整数：" + (random.nextInt(10) + 5));

    }
}
