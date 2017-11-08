package org.tic.vertx.examples.basic.example04;

import io.vertx.core.Vertx;

public class BasicExample04Launcher {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(SenderVerticle.class.getName());
        vertx.deployVerticle(ReceiverVerticle.class.getName());
    }

}
