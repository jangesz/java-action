package org.tic.concurrent.lock.redisson;

/**
 * 描述信息：分布式锁回调接口
 */
public interface DistributedLockCallback<T> {

    /**
     * 调用者必须在此方法中实现需要加分布式锁的业务逻辑
     */
    T process();


    /**
     * 获取分布式锁名称
     */
    String getLockName();

}
