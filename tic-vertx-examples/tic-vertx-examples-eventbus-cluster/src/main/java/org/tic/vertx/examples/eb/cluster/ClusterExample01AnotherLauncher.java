package org.tic.vertx.examples.eb.cluster;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClusterExample01AnotherLauncher {
    private static final Logger logger = LoggerFactory.getLogger(ClusterExample01AnotherLauncher.class);

    public static void main(String[] args) {
        VertxOptions options = new VertxOptions();
        Vertx.clusteredVertx(options, res -> {
            if (res.succeeded()) {
                Vertx vertx = res.result();
                vertx.deployVerticle(ThirdVerticle.class.getName());
                EventBus eventBus = vertx.eventBus();
                logger.debug("We now have a clustered event bus: " + eventBus);
            } else {
                logger.error("Failed: " + res.cause());
            }
        });
    }
}
