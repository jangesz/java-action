package org.tic.vertx.examples.service.discovery.common;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

public class MyEventBusServiceImpl implements MyEventBusService {
    @Override
    public MyEventBusService add(int a, int b, Handler<AsyncResult<Integer>> resultHandler) {
        int c = a + b;
        resultHandler.handle(Future.succeededFuture(c));
        return this;
    }
}
