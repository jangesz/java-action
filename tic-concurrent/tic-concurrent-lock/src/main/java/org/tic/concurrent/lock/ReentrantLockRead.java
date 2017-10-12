package org.tic.concurrent.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 描述：//TODO
 * <p>
 * 作者：Jange Gu
 * 网址：http://1gb.tech
 * http://jange.me
 * 创建时间：17/10/1.
 */
public class ReentrantLockRead {

    private static ReentrantLock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
//        lock.lock();
//        lock.unlock();

        lock.lock();
        lock.unlock();
        condition.await();

        condition.signal();

    }


}
