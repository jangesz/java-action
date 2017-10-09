package org.tic.concurrent.lock;

/**
 * 描述：//TODO
 * <p>
 * 作者：Jange Gu
 * 网址：http://1gb.tech
 * http://jange.me
 * 创建时间：17/10/1.
 */
public class SpinLockTask implements Runnable {

//    private SpinLockBetter lock = new SpinLockBetter();
    private SpinLock lock = new SpinLock();


    @Override
    public void run() {
        get();
    }

    private void get() {
        lock.lock();
        System.out.println(Thread.currentThread().getId());
        set();
        lock.unlock();
    }

    private void set() {
        lock.lock();
        System.out.println(Thread.currentThread().getId());
        lock.unlock();
    }
}