package com.mr.study.reentrantlock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhanxp
 * @version 1.0 2019/8/9
 */
public class ProducerConsumerTest {
    private ReentrantLock lock = new ReentrantLock();
    private Condition producer = lock.newCondition();
    private Condition consumer = lock.newCondition();
    private boolean hasValue = false;


    private void produce() throws InterruptedException {
        lock.lock();
        if (hasValue) {
            producer.await();
        }
        System.out.println("生产了一个。。。");
        consumer.signal();
        hasValue = true;
        lock.unlock();
    }

    private void consume() throws InterruptedException {
        lock.lock();
        if (!hasValue) {
            consumer.await();
        }
        System.out.println("消费了一个。。。");
        producer.signal();
        hasValue = false;
        lock.unlock();
    }

    public static void main(String[] args) {
        ProducerConsumerTest test = new ProducerConsumerTest();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    test.consume();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            new Thread(() -> {
                try {
                    test.produce();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
