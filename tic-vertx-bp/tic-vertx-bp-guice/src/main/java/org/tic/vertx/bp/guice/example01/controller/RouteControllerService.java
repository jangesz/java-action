package org.tic.vertx.bp.guice.example01.controller;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public interface RouteControllerService {
    String mountPoint();

    Router router(Vertx vertx);
}
