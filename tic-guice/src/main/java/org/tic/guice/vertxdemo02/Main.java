package org.tic.guice.vertxdemo02;

import io.vertx.core.Vertx;

public class Main {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(MainVerticle.class.getName());
    }

}
