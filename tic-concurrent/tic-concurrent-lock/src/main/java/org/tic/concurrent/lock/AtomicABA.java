package org.tic.concurrent.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 描述信息：CAS ABA问题
 */
public class AtomicABA {

    private static AtomicInteger atomicInteger = new AtomicInteger(100);
    private static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(100, 0);

    public static void main(String[] args) throws InterruptedException {
        //使用AtomicInteger，会出现ABA问题
        Thread t1 = new Thread(() -> {
            atomicInteger.compareAndSet(100, 101);
            atomicInteger.compareAndSet(101, 100);
        });

        Thread t2 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ignored) {}
            boolean flag = atomicInteger.compareAndSet(100, 101);
            //结果是true
            System.out.println(flag);
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();


        //使用AtomicStampedReference，不会出现ABA问题，因为加了版本控制
        Thread t3 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ignored) {}
            atomicStampedReference.compareAndSet(100, 101, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            atomicStampedReference.compareAndSet(101, 100, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
        });

        Thread t4 = new Thread(() -> {
            int stamp = atomicStampedReference.getStamp();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ignored) {}
            boolean flag = atomicStampedReference.compareAndSet(100, 101, stamp, stamp + 1);
            //结果是false
            System.out.println(flag);
        });

        t3.start();
        t4.start();
        t3.join();
        t4.join();
    }

}



























