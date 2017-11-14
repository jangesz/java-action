package org.tic.vertx.bp.modular.route.shared;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;

public class HttpOneVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        final Router router = Router.router(vertx);
        router.get("/one").handler(ctx -> ctx.response().end("OK one"));

        // mount the router as subrouter to the shared router
        final Router main = ShareableRouter.router(vertx).mountSubRouter("/", router);

        vertx.createHttpServer()
                .requestHandler(main::accept)
                .listen(8080, res -> {
                    if(res.succeeded()){
                        startFuture.complete();
                    } else {
                        startFuture.fail(res.cause());
                    }
                });
    }
}
