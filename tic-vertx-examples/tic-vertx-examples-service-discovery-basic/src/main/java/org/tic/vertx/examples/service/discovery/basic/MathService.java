package org.tic.vertx.examples.service.discovery.basic;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

@ProxyGen
public interface MathService {

    // A couple of factory methods to create an instance and a proxy
    static MathService create(Vertx vertx) {
        return new MathServiceImpl(vertx);
    }

    static MathService createProxy(Vertx vertx, String address) {
        return new MathServiceVertxEBProxy(vertx, address);
    }

    @Fluent
    MathService add(int a, int b, Handler<AsyncResult<Integer>> resultHandler);

}
