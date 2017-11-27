package org.tic.guice.vertxdemo02;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.AbstractVerticle;
import org.tic.guice.vertxdemo01.action.VertxDemo01Action;
import org.tic.guice.vertxdemo02.action.VertxDemo02Action;
import org.tic.guice.vertxdemo02.module.VertxDemo02Module;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        Injector injector = Guice.createInjector();

        System.out.println(injector.getInstance(VertxDemo02Action.class));
        System.out.println(injector.getInstance(VertxDemo01Action.class));

        vertx.setPeriodic(2000, h -> {
            System.out.println(injector.getInstance(VertxDemo02Action.class));
            System.out.println(injector.getInstance(VertxDemo01Action.class));
        });
    }
}
