package org.tic.vertx.examples.basic.example04;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.DeliveryOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceiverVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(ReceiverVerticle.class);

    @Override
    public void start() throws Exception {

        logger.debug(ReceiverVerticle.class.getName() + " has been deployed succeed!");

        vertx.eventBus().consumer("hello", message -> {
            logger.debug("consume hello message, data = " + message.body().toString());
            message.reply("thank you, my name is vert.x eventbus!", ar -> {
                if (ar.succeeded()) {
                    logger.debug("reply message ok!");
                } else {
                    logger.error(ar.cause().getMessage());
                }
            });
        });
    }

}