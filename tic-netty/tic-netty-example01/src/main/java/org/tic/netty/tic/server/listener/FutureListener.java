package org.tic.netty.tic.server.listener;

import org.tic.netty.tic.server.api.Listener;
import org.tic.netty.tic.server.exception.ServerException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 描述信息：
 * Jange Gu
 * http://jange.me
 * http://1gb.tech
 * 2017/9/26.
 */
public class FutureListener  extends CompletableFuture<Boolean> implements Listener {
    private final Listener listener;
    private final AtomicBoolean started;

    public FutureListener(AtomicBoolean started) {
        this.started = started;
        this.listener = null;
    }

    public FutureListener(Listener listener, AtomicBoolean started) {
        this.listener = listener;
        this.started = started;
    }

    @Override
    public void onSuccess(Object... args) {
        if (isDone()) return;// 防止Listener被重复执行
        complete(started.get());
        if (listener != null) listener.onSuccess(args);
    }

    @Override
    public void onFailure(Throwable cause) {
        if (isDone()) return;// 防止Listener被重复执行
        completeExceptionally(cause);
        if (listener != null) listener.onFailure(cause);
        throw cause instanceof ServerException
                ? (ServerException) cause
                : new ServerException(cause);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }
}