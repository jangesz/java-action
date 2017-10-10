package org.tic.concurrent.lock;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 描述信息：//TODO
 * Jange Gu
 * http://jange.me
 * http://1gb.tech
 * 2017/10/10.
 */
public class ConditionExample01 {

    private static Lock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    public static void main(String[] args) {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                System.out.println(Thread.currentThread().getName()+"正在运行......");
                try {
//                    Thread.sleep(2000);
                    System.out.println(Thread.currentThread().getName()+"停止运行，等待一个signal");
                    condition.await();      //调用该await后，lock的锁释放了。调用await()方法的前提是必须在lock.lock()下，否则会报IllegalMonitorStateException异常
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName()+"获得一个signal，继续运行");
                lock.unlock();
            }
        }, "waitThread");
        thread1.start();

//        try {
//            Thread.sleep(1000);     //保证线程1先执行，否则线程1将一直等待signal信号
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                System.out.println(Thread.currentThread().getName()+"正在运行......");
                condition.signal();     //发送信号，唤醒其它线程
                System.out.println(Thread.currentThread().getName()+"发送一个signal");
                System.out.println(Thread.currentThread().getName()+"发送一个signal后，结束");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.unlock();
            }
        }, "signalThread");
        thread2.start();
    }

}
