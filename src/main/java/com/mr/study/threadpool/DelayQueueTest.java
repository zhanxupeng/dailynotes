package com.mr.study.threadpool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author zhanxp
 * @version 1.0 2019/6/24
 */
public class DelayQueueTest {
    public static void main(String[] args) {
        //方便自己重命名线程名,方便查找问题
        ThreadFactory businessThreadFactory = new ThreadFactoryBuilder().setNameFormat("delay-queue-pool-%d").build();

        // 创建延时队列
        DelayQueue<Message> queue = new DelayQueue<Message>();
        // 添加延时消息,m1 延时3s
        Message m1 = new Message(1, "world", 2000);
        // 添加延时消息,m2 延时10s
        Message m2 = new Message(2, "hello", 5000);
        //将延时消息放到延时队列中
        queue.offer(m2);
        queue.offer(m1);
        // 启动消费线程 消费添加到延时队列中的消息，前提是任务到了延期时间
        ExecutorService exec = Executors.newFixedThreadPool(1, businessThreadFactory);
        exec.execute(new Consumer(queue));
        exec.shutdown();
    }
}
