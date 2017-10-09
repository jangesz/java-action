package org.tic.concurrent.lock;

import java.util.concurrent.locks.LockSupport;

/**
 * 描述：
 * <p> 问题：LockSupport.park()、LockSupport.unpark()，与object.wait()、notify()的区别？
 * <p> 回答：1. 面向的主体不一样。LockSupport主要是针对Thread进行阻塞处理，可以指定阻塞队列的目标对象，每次可以指定
 * <p> 具体的线程唤醒。Object.wait()是以对象为纬度的，阻塞当前的线程和唤醒单个（随机）或者所有线程。
 * <p>      2. 实现机制不同。虽然LockSupport可以指定monitor的object对象，但和Object.wait()比较，两者的阻塞队列并
 * <p> 不交叉。比如：Object.notifyAll()不能唤醒LockSupport的阻塞Thread。
 * <p>
 * <p> 问题：Thread.interrupt()会中断线程什么状态的工作？RUNNING or BLOCKING？
 * <p> 回答：1. 主要用于处理线程的BLOCK状态，
 * 作者：Jange Gu
 * 网址：http://1gb.tech
 * http://jange.me
 * 创建时间：17/10/5.
 */
public class LockSupportRead {

    public static void main(String[] args) {
        /*
        * 许可默认是被占用的，调用park()时获取不到许可，所以进入阻塞状态。
        */
//        LockSupport.park();
//        System.out.println("block.");

        /*
        * 先释放许可，再获取许可，主线程能够正常终止。LockSupport许可的获取和释放，一般来说是对应的，
        * 如果多次unpark，只有一次park也不会出现什么问题，结果是许可处于可用状态。
        */
//        Thread thread = Thread.currentThread();
//        LockSupport.unpark(thread); //释放许可
//        LockSupport.park();         //获取许可
//        System.out.println("b");


        /*
        *
        *
        *
        *
        */
//        Thread thread = Thread.currentThread();
//        LockSupport.unpark(thread);
//        System.out.println("a");
//        LockSupport.park();
//        System.out.println("b");
//        LockSupport.park();
//        System.out.println("c");

    }



}
