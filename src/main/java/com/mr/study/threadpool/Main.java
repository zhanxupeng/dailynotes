package com.mr.study.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author zhanxp
 * @version 1.0 2019/6/24
 */
public class Main {
    public static void main(String[] args) {
        //固定池子中线程的个数
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        //创建之初里面一个线程都没有，当execute方法或submit方法向线程池提交任务时，
        // 会自动新建线程；如果线程池中有空余线程，则不会新建；
        // 这种线程池一般最多情况可以容纳几万个线程，里面的线程空余60s会被回收
        ExecutorService executorService1 = Executors.newCachedThreadPool();

        //池中只有一个线程，如果扔5个任务进来，那么有4个任务将排队；作用是保证任务的顺序执行
        ExecutorService executorService2 = Executors.newSingleThreadExecutor();

        //定时执行
        ScheduledExecutorService executorService3 = Executors.newScheduledThreadPool(5);

        //todo DelayedWorkQueue待研究
    }
}
