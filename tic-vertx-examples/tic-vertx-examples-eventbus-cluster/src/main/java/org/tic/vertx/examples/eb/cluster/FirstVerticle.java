package org.tic.vertx.examples.eb.cluster;

import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirstVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(FirstVerticle.class);

    @Override
    public void start() throws Exception {

        logger.debug(FirstVerticle.class.getName() + " has been deployed succeed!");

        vertx.eventBus().consumer("hello", message -> {
            logger.debug("consume hello message, data = " + message.body().toString());
        });
    }

    private void sendBus(String address, Object data) {
        vertx.eventBus().send(address, data);
    }
}
