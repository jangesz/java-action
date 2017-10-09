package org.tic.concurrent.lock;

import java.util.concurrent.locks.LockSupport;

/**
 * 描述：//TODO
 * <p>
 * 作者：Jange Gu
 * 网址：http://1gb.tech
 * http://jange.me
 * 创建时间：17/10/8.
 */
public class LockSupportExample01 {

    /**
     * 下面这段代码说明了：线程如果因为调用park而阻塞的话，能够响应中断请求（中断状态被设置成true），
     * 但是不会抛出InterruptedException。
     *
     * 测试一："t.interrupt"代码注释掉，子线程t会一直阻塞，因为在子线程内部调用了LockSupport.park();
     *
     * 测试二："t.interrupt"代码不加注释，主线程main，和子线程t都会终止，子线程t（会在5s后终止，因为主线程睡眠了5s，然后调用t.interrupt）
     */
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new Runnable() {

            private int count = 0;

            @Override
            public void run() {
                long start = System.currentTimeMillis();
                long end = 0;
                while ((end - start) <= 1000) {
                    count++;
                    end = System.currentTimeMillis();
                }
                System.out.println("after 1 second.count = " + count);

                //等待获取许可
                LockSupport.park();

                System.out.println("thread over." + Thread.currentThread().isInterrupted());
            }
        });

        t.start();

        Thread.sleep(5000);

        // 中断线程
        t.interrupt();

        System.out.println("main over");
    }

}
