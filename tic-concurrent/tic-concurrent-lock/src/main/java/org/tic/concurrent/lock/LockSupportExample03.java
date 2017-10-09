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
public class LockSupportExample03 {
    public static Object lock = new Object();

    static ChangeObjectThread t1 = new ChangeObjectThread("t1");
    static ChangeObjectThread t2 = new ChangeObjectThread("t2");

    public static class ChangeObjectThread extends Thread {

        public ChangeObjectThread(String name) {
            super.setName(name);
        }

        @Override
        public void run() {
            // lock以一个静态对象作为锁
            // 这里使用park()
            synchronized (lock) {
                System.out.println("in => " + getName());
//                    lock.wait();
                    t1.suspend();
                    System.out.println("in release => " + getName());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        t1.start();
        Thread.sleep(100);
        t2.start();

        t1.resume();
        t2.resume();
//        LockSupport.unpark(t1);
//        LockSupport.unpark(t2);
        t1.join();
        t2.join();
        LockSupport.park(Thread.currentThread());
    }

}
