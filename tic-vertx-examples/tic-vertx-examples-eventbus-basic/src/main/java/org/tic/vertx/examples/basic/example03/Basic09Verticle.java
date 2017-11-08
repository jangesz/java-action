package org.tic.vertx.examples.basic.example03;

import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Basic09Verticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(Basic09Verticle.class);

    @Override
    public void start() throws Exception {

        logger.debug(Basic09Verticle.class.getName() + " has been deployed succeed!");

        vertx.setPeriodic(2000, h -> {
            publishBus("hello", "my name is vert.x");
        });

    }

    private void publishBus(String address, Object data) {
        vertx.eventBus().publish(address, data);
    }

}
