package org.tic.vertx.examples.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class WebRouterBlockingEventLoopExample04Verticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);

        router.route().handler(this::apiProduct);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(9999);
    }

    private void apiProduct(RoutingContext context) {
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        context.response().end("I blocked the eventloop thread which means I broken the gold line. That's terrible !");
    }

}
