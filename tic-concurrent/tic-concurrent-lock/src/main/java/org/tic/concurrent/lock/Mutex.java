package org.tic.concurrent.lock;

import java.util.concurrent.locks.AbstractQueuedLongSynchronizer;

/**
 * 描述信息：//TODO
 * Jange Gu
 * http://jange.me
 * http://1gb.tech
 * 2017/10/12.
 */
public class Mutex {

    private static class Sync extends AbstractQueuedLongSynchronizer {

        //判断是否锁的状态
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        // 尝试获取资源，立即返回。成功则返回true，否则false。
        @Override
        protected boolean tryAcquire(long acquires) {
            assert acquires == 1;
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        // 尝试释放资源，立即返回。成功则为true，否则false。
        @Override
        protected boolean tryRelease(long releases) {
            assert releases == 1;
            if (getState() == 0)
                throw new IllegalMonitorStateException();
            setExclusiveOwnerThread(null);
            setState(0);    //释放资源，放弃占有状态
            return true;
        }
    }

}
