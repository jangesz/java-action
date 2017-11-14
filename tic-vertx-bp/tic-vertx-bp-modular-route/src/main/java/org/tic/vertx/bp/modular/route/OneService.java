package org.tic.vertx.bp.modular.route;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class OneService implements ServiceEndpoint {
    @Override
    public String mountPoint() {
        return "/1";
    }

    @Override
    public Router router(Vertx vertx) {
        Router router = Router.router(vertx);
        router.get("/one").handler(ctx -> ctx.response().end("One OK"));
        return router;
    }
}
