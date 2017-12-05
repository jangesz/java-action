package org.tic.vertx.examples.web.ueditor;

import io.vertx.core.Vertx;

public class Launcher {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(UeditorVerticle.class.getName());
    }

}
