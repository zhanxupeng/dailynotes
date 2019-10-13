package com.mr.study.threadpool;

import java.util.concurrent.DelayQueue;

/**
 * @author zhanxp
 * @version 1.0 2019/6/25
 */
public class Consumer implements Runnable {

    private DelayQueue<Message> delayQueue;

    public Consumer(DelayQueue<Message> delayQueue) {
        this.delayQueue = delayQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("当前线程的名称为：" + Thread.currentThread().getName());
                Message message = delayQueue.take();
                System.out.println("当前的消息为:" + message.getBody() + ",时间为" + System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
