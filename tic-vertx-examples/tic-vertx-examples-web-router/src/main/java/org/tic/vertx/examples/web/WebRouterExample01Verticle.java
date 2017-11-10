package org.tic.vertx.examples.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebRouterExample01Verticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(WebRouterExample01Verticle.class);

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.get("/api/product").handler(this::apiProduct);
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

    private void apiProduct(RoutingContext context) {
        context.response().end(context.request().absoluteURI() + " request ok !");
    }

}
