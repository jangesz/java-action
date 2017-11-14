package org.tic.vertx.bp.modular.route;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

public class ServerVerticle extends AbstractVerticle {

    @Override
    public void start(final Future<Void> startFuture) throws Exception {
        //create a service loader for the ServiceEndpoints
        ServiceLoader<ServiceEndpoint> loader = ServiceLoader.load(ServiceEndpoint.class);

        //iterate over all endpoints and mount all their endpoints to a single router
        Router main = StreamSupport.stream(loader.spliterator(), false)
                .collect(() -> Router.router(vertx), //the main router
                        (r, s) -> r.mountSubRouter(s.mountPoint(), s.router(vertx)),
                        (r1, r2) -> {});

        //bind the main router to the http server
        vertx.createHttpServer().requestHandler(main::accept).listen(8080, res -> {
            if (res.succeeded()) {
                startFuture.complete();
            } else {
                startFuture.fail(res.cause());
            }
        });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(ServerVerticle.class.getName());
    }

}
