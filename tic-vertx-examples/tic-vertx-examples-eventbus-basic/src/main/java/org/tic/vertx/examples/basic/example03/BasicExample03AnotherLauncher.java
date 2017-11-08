package org.tic.vertx.examples.basic.example03;

import io.vertx.core.Vertx;

public class BasicExample03AnotherLauncher {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(Basic10Verticle.class.getName());
    }

}
