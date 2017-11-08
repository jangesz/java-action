package org.tic.vertx.examples.eb.cluster;

import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThirdVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(ThirdVerticle.class);

    @Override
    public void start() throws Exception {

        logger.debug(ThirdVerticle.class.getName() + " has been deployed succeed!");

        vertx.setPeriodic(2000, h -> {
            sendBus("hello", "my name is vert.x");
        });

    }

    private void sendBus(String address, Object data) {
        vertx.eventBus().send(address, data);
    }

}
