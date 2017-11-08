package org.tic.vertx.examples.basic.example01;

import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Basic03Verticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(Basic03Verticle.class);

    @Override
    public void start() throws Exception {

        logger.debug(Basic03Verticle.class.getName() + " has been deployed succeed!");

        vertx.setPeriodic(2000, h -> {
            sendBus("hello", "my name is vert.x");
        });

    }

    private void sendBus(String address, Object data) {
        vertx.eventBus().send(address, data);
    }

}
