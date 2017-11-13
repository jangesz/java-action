package org.tic.vertx.examples.service.discovery.common;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

@ProxyGen
public interface MyEventBusService {

    @Fluent
    MyEventBusService add(int a, int b, Handler<AsyncResult<Integer>> resultHandler);

}
