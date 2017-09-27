package org.tic.netty.tic.server.api;

/**
 * 描述信息：//TODO
 * Jange Gu
 * http://jange.me
 * http://1gb.tech
 * 2017/9/26.
 */
public interface Listener {
    void onSuccess(Object... args);

    void onFailure(Throwable throwable);
}
