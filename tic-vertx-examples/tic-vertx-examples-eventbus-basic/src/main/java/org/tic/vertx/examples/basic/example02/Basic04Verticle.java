package org.tic.vertx.examples.basic.example02;

import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Basic04Verticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(Basic04Verticle.class);

    @Override
    public void start() throws Exception {

        logger.debug(Basic04Verticle.class.getName() + " has been deployed succeed!");

        vertx.eventBus().consumer("hello", message -> {
            logger.debug("consume hello message, data = " + message.body().toString());
        });
    }

    private void sendBus(String address, Object data) {
        vertx.eventBus().send(address, data);
    }
}
