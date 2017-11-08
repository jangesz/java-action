package org.tic.vertx;


import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {


    private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);

    @Override
    public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();

        server.requestStream().toObservable()
                .subscribe(req -> {
                    logger.debug("first server verticle!");
                    req.response().end("Hello from " + Thread.currentThread().getName());
                });

        server.rxListen(3456).subscribe();


    }
}
