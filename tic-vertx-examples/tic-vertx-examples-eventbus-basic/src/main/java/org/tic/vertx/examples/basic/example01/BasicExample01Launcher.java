package org.tic.vertx.examples.basic.example01;

import io.vertx.core.Vertx;

public class BasicExample01Launcher {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(Basic01Verticle.class.getName());
        vertx.deployVerticle(Basic02Verticle.class.getName());
        vertx.deployVerticle(Basic03Verticle.class.getName());
    }

}
