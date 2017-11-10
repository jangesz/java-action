package org.tic.vertx.examples.web;

import io.vertx.core.Vertx;

public class Launcher {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(WebRouterExample01Verticle.class.getName());
    }

}
