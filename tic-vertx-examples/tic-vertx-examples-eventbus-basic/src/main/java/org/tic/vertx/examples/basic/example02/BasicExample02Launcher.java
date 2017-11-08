package org.tic.vertx.examples.basic.example02;

import io.vertx.core.Vertx;

public class BasicExample02Launcher {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(Basic04Verticle.class.getName());
        vertx.deployVerticle(Basic05Verticle.class.getName());
        vertx.deployVerticle(Basic06Verticle.class.getName());
    }

}
