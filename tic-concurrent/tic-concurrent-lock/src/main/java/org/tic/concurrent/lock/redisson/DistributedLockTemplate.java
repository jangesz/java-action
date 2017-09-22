package org.tic.concurrent.lock.redisson;

import java.util.concurrent.TimeUnit;

/**
 * 描述信息：分布式锁操作模板
 */
public interface DistributedLockTemplate {

    /**
     * 使用分布式锁，使用锁默认的超时时间
     * @param callback 回调函数，应该在这个函数中执行需要加分布式锁的逻辑
     */
    <T> T lock(DistributedLockCallback<T> callback);

    /**
     * 使用分布式锁，自定义锁的超时时间
     * @param callback 回调函数，应该在这个函数中执行需要加分布式锁的逻辑
     * @param leaseTime 锁超时时间，超时后自动释放锁
     * @param timeUnit 时间单位
     */
    <T> T lock(DistributedLockCallback<T> callback, long leaseTime, TimeUnit timeUnit);

}
