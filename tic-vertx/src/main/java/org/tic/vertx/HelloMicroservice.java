package org.tic.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

public class HelloMicroservice extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.get("/").handler(rc -> rc.response().end("hello"));
        router.get("/:name").handler(rc -> rc.response().end("hello " + rc.pathParam("name")));

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(3456);


    }
}
