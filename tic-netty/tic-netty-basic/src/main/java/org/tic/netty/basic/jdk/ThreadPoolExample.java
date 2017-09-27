package org.tic.netty.basic.jdk;

import io.netty.util.concurrent.SingleThreadEventExecutor;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * 描述信息：//TODO
 * Jange Gu
 * http://jange.me
 * http://1gb.tech
 * 2017/9/26.
 */
public class ThreadPoolExample {
    private static boolean stop;

    @Test
    public void test01() throws InterruptedException {
    }


    public static void main(String[] args) throws InterruptedException {
        Thread workThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while(!stop) {
                    i++;
                    try {
                        System.out.println(i);
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        workThread.start();
        TimeUnit.SECONDS.sleep(3);
        stop = true;
    }
}
