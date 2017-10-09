package org.tic.concurrent.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 描述：http://waitingkkk-163-com.iteye.com/blog/2240412
 * <p>
 * 作者：Jange Gu
 * 网址：http://1gb.tech
 * http://jange.me
 * 创建时间：17/10/8.
 */
public class DeadLockExample {

    private static Lock lockA = new ReentrantLock();
    private static Lock lockB = new ReentrantLock();

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lockA.lock();
                    TimeUnit.SECONDS.sleep(2);

                    try {
                        lockB.lock();
                    } finally {
                        lockB.unlock();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lockA.unlock();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lockB.lock();
                    TimeUnit.SECONDS.sleep(2);
                    try {
                        lockA.lock();
                    } finally {
                        lockA.unlock();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lockB.unlock();
                }
            }
        }).start();
    }

}
