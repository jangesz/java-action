package org.tic.concurrent.lock;

/**
 * 描述：//TODO
 * <p>
 * 作者：Jange Gu
 * 网址：http://1gb.tech
 * http://jange.me
 * 创建时间：17/10/1.
 */
public class SpinLockTest {

    public static void main(String[] args) {
        SpinLockTask test = new SpinLockTask();
        new Thread(test).start();
        new Thread(test).start();
        new Thread(test).start();
    }

}
