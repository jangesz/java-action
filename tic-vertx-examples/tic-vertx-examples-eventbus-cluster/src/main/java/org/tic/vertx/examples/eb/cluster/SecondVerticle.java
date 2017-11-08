package org.tic.vertx.examples.eb.cluster;

import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecondVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(SecondVerticle.class);

    @Override
    public void start() throws Exception {

        logger.debug(SecondVerticle.class.getName() + " has been deployed succeed!");

        vertx.eventBus().consumer("hello", message -> {
            logger.debug("consume hello message, data = " + message.body().toString());
        });
    }

    private void sendBus(String address, Object data) {
        vertx.eventBus().send(address, data);
    }
}
