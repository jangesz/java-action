package org.tic.vertx.chapter01;

import io.vertx.core.Vertx;

public class VerticleFactoryTest {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(Chapter01Verticle.class.getName());
    }

}
