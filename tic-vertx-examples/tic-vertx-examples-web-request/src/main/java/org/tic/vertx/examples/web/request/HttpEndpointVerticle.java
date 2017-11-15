package org.tic.vertx.examples.web.request;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpEndpointVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(HttpEndpointVerticle.class);

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        //表单提交
        router.post("/account").handler(this::addAccount);
        //body提交

        vertx.createHttpServer()
            .requestHandler(router::accept)
            .listen(9999, res -> {
                if (res.succeeded()) {
                    logger.info("http endpoint server started in port 9999 !");
                } else {
                    logger.error("http endpoint server started failed !");
                }
            });
    }

    private void addAccount(RoutingContext context) {

        context.addBodyEndHandler(h -> logger.info("addBodyEndHandler"));
        context.addHeadersEndHandler(h -> logger.info("addHeadersEndHandler"));
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(HttpEndpointVerticle.class.getName());
    }

}
