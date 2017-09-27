package org.tic.netty.tic.server.api;

import io.netty.channel.EventLoopGroup;

/**
 * 描述信息：//TODO
 * Jange Gu
 * http://jange.me
 * http://1gb.tech
 * 2017/9/26.
 */
public interface NettyServer {
    String getBossThreadName();

    String getWorkerThreadName();

    EventLoopGroup getBossGroup();

    EventLoopGroup getWorkerGroup();

    int getBossThreadNum();

    int getWorkerThreadNum();

    int getIoRate();

}
