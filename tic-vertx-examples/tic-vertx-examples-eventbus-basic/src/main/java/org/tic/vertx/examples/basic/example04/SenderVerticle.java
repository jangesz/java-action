package org.tic.vertx.examples.basic.example04;

import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tic.vertx.examples.basic.example01.Basic03Verticle;
import org.tic.vertx.examples.basic.example03.Basic10Verticle;

public class SenderVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(SenderVerticle.class);

    @Override
    public void start() throws Exception {

        logger.debug(SenderVerticle.class.getName() + " has been deployed succeed!");

        vertx.setPeriodic(2000, h -> {
            sendBus("hello", "my name is vert.x");

            vertx.eventBus().send("hello", "my name is vert.x", reply -> {
                if (reply.succeeded()) {
                    logger.debug("receive reply message " + reply.result().body().toString());
                } else {
                    logger.error(reply.cause().getMessage());
                }
            });
        });

    }

    private void sendBus(String address, Object data) {
        vertx.eventBus().send(address, data);
    }

}
