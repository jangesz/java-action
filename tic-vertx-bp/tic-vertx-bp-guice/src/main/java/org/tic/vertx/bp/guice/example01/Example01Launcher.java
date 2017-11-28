package org.tic.vertx.bp.guice.example01;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.tic.vertx.bp.guice.example01.module.Example01Module;

public class Example01Launcher {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        deploy(vertx, Example01Verticle.class, new DeploymentOptions());
    }

    private static Future<Void> deploy(Vertx vertx, Class verticle, DeploymentOptions options) {
        Future<Void> future = Future.future();
        String deploymentName = "java-guice:" + verticle.getName();
        JsonObject config = new JsonObject()
                .put("guice_binder", Example01Module.class.getName());

        options.setConfig(config);

        vertx.deployVerticle(deploymentName, options, ar -> {
            if (ar.succeeded()) {
                System.out.println("deploy verticle " + verticle.getName() + " succeed!");
                future.complete();
            } else {
                System.out.println("deploy verticle " + verticle.getName() + " failed!");
                System.out.println("reason -> " + ar.cause());
                future.fail(ar.cause().getMessage());
            }
        });

        return future;
    }
}
