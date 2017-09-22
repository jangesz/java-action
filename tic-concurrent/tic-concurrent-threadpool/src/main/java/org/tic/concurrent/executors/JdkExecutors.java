package org.tic.concurrent.executors;

import org.junit.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 描述信息：//TODO
 * Jange Gu
 * http://jange.me
 * http://1gb.tech
 * 2017/9/22.
 */
public class JdkExecutors {

    public void test01() {
        Executors.newSingleThreadExecutor();
        Executors.newSingleThreadExecutor(new MyThreadFactory());

        Executors.newSingleThreadScheduledExecutor();
        Executors.newSingleThreadScheduledExecutor(new MyThreadFactory());

        Executors.newScheduledThreadPool(2);
        Executors.newScheduledThreadPool(2, new MyThreadFactory());

        Executors.newCachedThreadPool();
        Executors.newCachedThreadPool(new MyThreadFactory());

        Executors.newFixedThreadPool(2);
        Executors.newFixedThreadPool(2, new MyThreadFactory());

        Executors.newWorkStealingPool();

    }

    private class MyThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            return null;
        }
    }


    @Test
    public void test02() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("tom run run run!");
            }
        });
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("tom run run run!");
            }
        });
        //当executor执行了一个任务后，Java进程没有停止！Executors必须显示的停止，否则它们将继续监听新的任务
    }

}
