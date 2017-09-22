package org.tic.concurrent.async.jdk;

import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import com.sun.istack.internal.NotNull;
import org.junit.Test;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

/**
 * 描述信息：//TODO
 * Jange Gu
 * http://jange.me
 * http://1gb.tech
 * 2017/9/22.
 */
public class JdkRunnable {


    @Test
    public void test01() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> f = executor.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("123木头人");
                }
            });
            System.out.println(f);
            System.out.println("===>" + f.get());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    @Test
    public void test02() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<Integer> f = executor.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    Thread.sleep(6000);
                    return Integer.MAX_VALUE;
                }
            });
            System.out.println(f.isCancelled());
            System.out.println(f.isDone());
            System.out.println(f.get(5, TimeUnit.SECONDS));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    @Test
    public void test03() {
//        ExecutorService executor = Executors.newSingleThreadExecutor(new HandleThread());
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            executor.execute(new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("run run run boom exception! ");
                    throw new RuntimeException("boom exception occured");
                }
            }));
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    private class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println("Caught " + e);
        }
    }

    private class HandleThread implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
            return t;
        }
    }

}
