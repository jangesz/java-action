package org.tic.vertx.bp.modular.route.shared;

import io.vertx.core.Vertx;

public class Launcher {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(HttpOneVerticle.class.getName());

        // 延迟部署第二个路由（延迟10s）
        vertx.setTimer(10000, h -> {
            vertx.deployVerticle(HttpTwoVerticle.class.getName());
            System.out.println("HttpTwoVerticle deployed succeed !");
        });
    }

}
