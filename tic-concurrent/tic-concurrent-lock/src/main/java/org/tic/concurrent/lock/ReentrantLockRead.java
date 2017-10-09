package org.tic.concurrent.lock;

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


    public static void main(String[] args) {
        lock.lock();

        lock.unlock();
    }

}
