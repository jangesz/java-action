package org.tic.vertx.examples.service.discovery.basic;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

public class MathServiceImpl implements MathService {

    public MathServiceImpl(Vertx vertx) {
    }

    @Override
    public MathService add(int a, int b, Handler<AsyncResult<Integer>> resultHandler) {
        int c = a + b;
        resultHandler.handle(Future.succeededFuture(c));
        return this;
    }
}
