package org.tic.concurrent.thread;

import java.util.concurrent.CountDownLatch;

/**
 * 描述信息：//TODO
 * Jange Gu
 * http://jange.me
 * http://1gb.tech
 * 2017/9/22.
 */
public class ThreadGroupMain {

    public static void main(String[] args) {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        ThreadGroup parent = group.getParent();
        System.out.println(group.getName());
        System.out.println(parent);
        System.out.println(parent.getName());
        System.out.println(parent.getParent());
        //最顶的线程组名为system
        //主线程组的名称是main

        //创建一个线程
        Thread t = new Thread(parent, new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " i am running in threadgroup :" + parent.getName());
            }
        });


        Thread t2 = new Thread(group, new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " i am running in threadgroup : " + group.getName());
            }
        }, "main-sub-thread-2");


        ThreadGroup ticGroup = new ThreadGroup("tic-threadgroup");
        Thread t3 = new Thread(ticGroup, new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " i am running in threadgroup : " + ticGroup.getName());
            }
        }, "tic-sub-thread-3");

        CountDownLatch latch = new CountDownLatch(1);
        latch.countDown();
        t.start();
        t2.start();
        t3.start();
    }

}
