package org.tic.vertx.bp.guice.demo01;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class Main {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        Future<Void> future = deploy(vertx, ServiceLauncher.class, new DeploymentOptions());
    }

    private static Future<Void> deploy(Vertx vertx, Class verticle, DeploymentOptions options) {
        Future<Void> future = Future.future();
        String deploymentName = "java-guice:" + verticle.getName();
        JsonObject config = new JsonObject()
                .put("guice_binder", ServiceBinder.class.getName());

        options.setConfig(config);

        vertx.deployVerticle(deploymentName, options, ar -> {
            if (ar.succeeded()) {
                System.out.println("deploy verticle " + verticle.getName() + " succeed!");
                future.complete();
            } else {
                System.out.println("deploy verticle " + verticle.getName() + " failed!");
                future.fail(ar.cause().getMessage());
            }
        });

        return future;
    }

}
