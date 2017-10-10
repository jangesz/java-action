package org.tic.concurrent.lock;

import java.util.concurrent.CountDownLatch;

/**
 * 描述信息：//TODO
 * Jange Gu
 * http://jange.me
 * http://1gb.tech
 * 2017/10/10.
 */
public class CountDownLatchRead {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);

        latch.await();

        latch.countDown();
    }

}
