package com.mr.study.queue;

/**
 * @author zhanxp
 * @version 1.0 2019/8/8
 */
public class ArrayBlockingQueueTest {
    public static void main(String[] args) {
        SimpleArrayQueue<String> queue = new SimpleArrayQueue<>();
        queue.put("aaa");
        queue.put("bbb");
        queue.put("ccc");
        System.out.println(queue.get());
        System.out.println(queue.get());
        queue.put("ddd");
        queue.put("eee");
        queue.put("fff");
        System.out.println(queue.get());
        System.out.println(queue.get());
        queue.put("ggg");
        System.out.println(queue.get());
        System.out.println(queue.get());
        System.out.println(queue.get());
    }
}
