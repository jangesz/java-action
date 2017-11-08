package org.tic.vertx.reactive.microservice.hello.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloMicroservice extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(HelloMicroservice.class);

    @Override
    public void start() throws Exception {
        logger.debug(HelloMicroservice.class.getName() + " service started!");
//        vertx.eventBus().<String>consumer("hello", message -> {
//            logger.debug(" receive eventbus message " +  message.body());
//            JsonObject json = new JsonObject()
//                    .put("served-by", HelloMicroservice.class.toString());
//
//            if (message.body().isEmpty()) {
//                message.reply(json.put("message", "hello"));
//            } else {
//                message.reply(json.put("message", "hello " + message.body()));
//            }
//        }).completionHandler(ar -> {
//            if (ar.succeeded()) {
//                logger.debug("The handler registration has reached all nodes");
//            } else {
//                logger.error("Registration failed!");
//            }
//        });

        MessageConsumer<String> consumer = vertx.eventBus().consumer("hello");

        consumer.handler(message -> {
            logger.debug(" receive eventbus message " +  message.body());
            JsonObject json = new JsonObject()
                    .put("served-by", HelloMicroservice.class.toString());

            if (message.body().isEmpty()) {
                message.reply(json.put("message", "hello"));
            } else {
                message.reply(json.put("message", "hello " + message.body()));
            }
        });

        consumer.completionHandler(ar -> {
            if (ar.succeeded()) {
                logger.debug("The handler registration has reached all nodes");
            } else {
                logger.error("Registration failed!");
            }
        });

//        consumer.unregister(ar -> {
//            if (ar.succeeded()) {
//                logger.debug("The handler un-registration has reached all nodes");
//            } else {
//                logger.error("Un-registration failed!");
//            }
//        });



//        Router router = Router.router(vertx);
//        router.get("/").handler(this::hello);
//        router.get("/:name").handler(this::hello);
//
//        vertx.createHttpServer()
//                .requestHandler(router::accept)
//                .listen(3456);


    }

//    private void hello(RoutingContext rc) {
//        logger.debug("------ call hello service ------");
//        String message = "hello";
//        if (rc.pathParam("name") != null) {
//            message += " " + rc.pathParam("name");
//        }
//        JsonObject json = new JsonObject().put("message", message);
//        rc.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
//                .end(json.encode());
//    }


}
