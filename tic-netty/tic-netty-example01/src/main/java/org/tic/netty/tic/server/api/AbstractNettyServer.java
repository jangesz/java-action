package org.tic.netty.tic.server.api;

import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.ThreadFactory;

/**
 * 描述信息：//TODO
 * Jange Gu
 * http://jange.me
 * http://1gb.tech
 * 2017/9/26.
 */
public abstract class AbstractNettyServer implements NettyServer {

    @Override
    public EventLoopGroup getBossGroup() {
        return null;
    }

    @Override
    public EventLoopGroup getWorkerGroup() {
        return null;
    }

    @Override
    public int getWorkerThreadNum() {
        return 0;
    }

    @Override
    public int getBossThreadNum() {
        return 1;
    }

    @Override
    public String getWorkerThreadName() {
        return "netty-worker-threadgroup";
    }

    @Override
    public String getBossThreadName() {
        return "netty-boss-threadgroup";
    }

    /**
     * netty 默认的Executor为ThreadPerTaskExecutor
     * 线程池的使用在SingleThreadEventExecutor#doStartThread
     * <p>
     * eventLoop.execute(runnable);
     * 是比较重要的一个方法。在没有启动真正线程时，
     * 它会启动线程并将待执行任务放入执行队列里面。
     * 启动真正线程(startThread())会判断是否该线程已经启动，
     * 如果已经启动则会直接跳过，达到线程复用的目的
     */
     protected ThreadFactory getBossThreadFactory() {
         return new DefaultThreadFactory(getBossThreadName());
     }

     protected ThreadFactory getWorkerThreadFactory() {
         return new DefaultThreadFactory(getWorkerThreadName());
     }

    @Override
    public int getIoRate() {
        return 70;
    }
}
