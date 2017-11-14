package org.tic.vertx.bp.modular.route.shared;

import io.vertx.core.Vertx;
import io.vertx.core.shareddata.Shareable;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.impl.RouterImpl;

public class ShareableRouter extends RouterImpl implements Shareable {
    public ShareableRouter(Vertx vertx) {
        super(vertx);
    }

    // 该共享Router只能被同一个Vert.x实例中的不同Verticle共享，不适用于Cluter模式
    public static Router router(Vertx vertx) {
        return (Router) vertx.sharedData().getLocalMap("router").computeIfAbsent("main", n -> new ShareableRouter(vertx));
    }
}
