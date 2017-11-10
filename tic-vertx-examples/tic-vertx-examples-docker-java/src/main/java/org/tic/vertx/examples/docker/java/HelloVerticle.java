package org.tic.vertx.examples.docker.java;

import io.vertx.core.AbstractVerticle;

public class HelloVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        vertx.createHttpServer()
            .requestHandler(req -> {
                req.response().end("Hello Vert.x");
            })
            .listen(8080);
    }
}
