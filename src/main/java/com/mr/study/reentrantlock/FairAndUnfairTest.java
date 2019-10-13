package com.mr.study.reentrantlock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhanxp
 * @version 1.0 2019/8/9
 */
public class FairAndUnfairTest {
    private static Lock fairLock = new ReentrantLock2(true);
    private static Lock unfairLock = new ReentrantLock2(false);

    private void testLock(Lock lock) {
        for (int i = 0; i < 5; i++) {
            Job job = new Job(lock);
            job.setName("线程" + (i + 1));
            job.start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        FairAndUnfairTest test = new FairAndUnfairTest();
        test.testLock(fairLock);
//        test.testLock(unfairLock);
    }

    private static class Job extends Thread {
        private Lock lock;

        public Job(Lock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 2; i++) {
                lock.lock();
                try {
                    Thread.sleep(1000);
                    Collection<Thread> threads = ((ReentrantLock2) lock).getQueuedThreads();
                    System.out.println("获取锁的当前线程[" + Thread.currentThread().getName() + "],同步队列中的线程" + getThreadName(threads));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    private static String getThreadName(Collection<Thread> threads) {
        StringBuilder sb = new StringBuilder();
        if (threads != null) {
            for (Thread thread : threads) {
                sb.append(thread.getName()).append(",");
            }
        }
        String waitingTheads = null;
        if (sb.length() > 0) {
            waitingTheads = sb.substring(0, sb.length() - 1);
        }
        return waitingTheads;
    }

    private static class ReentrantLock2 extends ReentrantLock {
        public ReentrantLock2(boolean fair) {
            super(fair);
        }

        @Override
        public Collection<Thread> getQueuedThreads() {
            List<Thread> arrayList = new ArrayList<>(super.getQueuedThreads());
            Collections.reverse(arrayList);
            return arrayList;
        }

    }
}
