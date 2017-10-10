package org.tic.concurrent.lock;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 描述信息：//TODO
 * Jange Gu
 * http://jange.me
 * http://1gb.tech
 * 2017/10/10.
 */
public class CountDownLatchExample01 {
    private static ArrayBlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(10);
    private static CountDownLatch latch;
    private static final int ThreadNum = 10;

    static {
        for (int i = 0; i < 10; i++) {
            blockingQueue.add("test" + i);
        }
        latch = new CountDownLatch(10);
    }

    /**
     * 用blockQueue中的元素模拟文件分片
     * @return
     */
    public static String getFileSplit() {
        return blockingQueue.poll();
    }

    static class myThread implements Runnable {

        public void run() {
            System.out.println(Thread.currentThread().getName() + "begin running...");
            String m = getFileSplit();
            HashFunction hf = Hashing.md5();
            HashCode hc = hf.newHasher()
                    .putString(m, Charsets.UTF_8)
                    .hash();
            System.out.println(hc.toString());
            try {
                Thread.currentThread().sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
            System.out.println(Thread.currentThread().getName() + "ended");
        }
    }

    public static void main(String args[]){
        System.out.println("主线程开始运行");
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i=0;i<ThreadNum;i++){
            service.execute(new Thread(new myThread()));
        }
        service.shutdown();
        System.out.println("线程已经全部运行");
        System.out.println("等待所有线程运行结束");
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("主线程退出");
    }
}
