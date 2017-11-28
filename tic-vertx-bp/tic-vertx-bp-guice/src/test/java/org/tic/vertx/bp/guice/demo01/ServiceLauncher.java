package org.tic.vertx.bp.guice.demo01;

import io.vertx.core.AbstractVerticle;

import javax.inject.Inject;

public class ServiceLauncher extends AbstractVerticle {

    private AppConfig appConfig;

    @Inject
    public ServiceLauncher(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public void start() throws Exception {
        System.out.println("appconfig -> " + this.appConfig);

        vertx.setPeriodic(1000, h -> {
            System.out.println(this.appConfig);
        });
    }
}
