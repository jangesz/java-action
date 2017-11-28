package org.tic.vertx.bp.guice.example01.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.tic.vertx.bp.guice.example01.controller.RouteController;

public class RouteControllerModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    public RouteController routeController() {
        return new R
    }

}
