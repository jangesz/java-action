package org.tic.vertx.examples.service.discovery.basic;

import io.vertx.core.Vertx;

public class Launcher {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

//        vertx.deployVerticle(EventBusServiceDiscoveryExample.class.getName());
//        vertx.deployVerticle(EventBusSimpleServiceDiscoveryVerticle.class.getName());
        vertx.deployVerticle(JDBCDatasourceSimpleServiceDiscoveryVerticle.class.getName());
    }

}
