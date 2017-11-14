package org.tic.vertx.bp.modular.route.shared;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;

public class HttpTwoVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        final Router router = Router.router(vertx);
        router.get("/two").handler(ctx -> ctx.response().end("OK two"));

        // mount the router as subrouter to the shared router
        final Router main = ShareableRouter.router(vertx).mountSubRouter("/", router);
    }
}