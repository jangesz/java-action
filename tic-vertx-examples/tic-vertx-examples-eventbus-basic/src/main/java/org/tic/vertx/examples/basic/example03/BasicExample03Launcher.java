package org.tic.vertx.examples.basic.example03;

import io.vertx.core.Vertx;

public class BasicExample03Launcher {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(Basic07Verticle.class.getName());
        vertx.deployVerticle(Basic08Verticle.class.getName());
        vertx.deployVerticle(Basic09Verticle.class.getName());
    }

}
