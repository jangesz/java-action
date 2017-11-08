package org.tic.vertx.examples.basic.example01;

import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Basic01Verticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(Basic01Verticle.class);

    @Override
    public void start() throws Exception {

        logger.debug(Basic01Verticle.class.getName() + " has been deployed succeed!");

        vertx.eventBus().consumer("hello", message -> {
            logger.debug("consume hello message, data = " + message.body().toString());
        });
    }

    private void sendBus(String address, Object data) {
        vertx.eventBus().send(address, data);
    }
}
