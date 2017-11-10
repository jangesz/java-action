package org.tic.vertx.examples.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebRouterNextExample02Verticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(WebRouterNextExample02Verticle.class);

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.get("/api/product").handler(this::apiProductPart1);
        router.get("/api/product").handler(this::apiProductPart2);
        router.get("/api/product").handler(this::apiProductPart3);
        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(9999, ar -> {
                    if (ar.succeeded()) {
                        logger.info("verticle started succeed at port 9999 !");
                    } else {
                        logger.error("verticle started failed !");
                    }
                });
    }

    private void apiProductPart1(RoutingContext context) {
        context.response().setChunked(true).write()
    }

}
