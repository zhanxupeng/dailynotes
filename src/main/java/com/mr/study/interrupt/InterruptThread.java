package com.mr.study.interrupt;

/**
 * @author zhanxp
 * @version 1.0 2019/8/8
 */
public class InterruptThread extends Thread {
    public static void main(String[] args) throws Exception {
        InterruptThread thread = new InterruptThread();
        thread.start();
//        Thread.sleep(3000);
//
//        thread.interrupt();
//        Thread.sleep(3000);

    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("Thread is running...");
            long time = System.currentTimeMillis();
            while ((System.currentTimeMillis() - time) < 1000) {

            }
        }
        System.out.println("Thread exiting under request...");
    }
}
