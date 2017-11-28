package org.tic.vertx.bp.guice.demo01.verticles;

import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tic.vertx.bp.guice.demo01.AppConfig;

import javax.inject.Inject;

public class MainVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);

    private AppConfig appConfig;

    @Inject
    public MainVerticle(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public void start() throws Exception {
        vertx.setPeriodic(1000, h -> {
            logger.info("appConfig instance => " + this.appConfig);
        });
    }
}
