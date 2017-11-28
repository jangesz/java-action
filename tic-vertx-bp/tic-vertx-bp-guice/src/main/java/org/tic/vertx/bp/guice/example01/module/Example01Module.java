package org.tic.vertx.bp.guice.example01.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.tic.vertx.bp.guice.example01.controller.Example01DogController;
import org.tic.vertx.bp.guice.example01.controller.RouteController;
import org.tic.vertx.bp.guice.example01.dao.Example01PetStoreDao;
import org.tic.vertx.bp.guice.example01.dao.impl.Example01PetStoreDaoImpl;
import org.tic.vertx.bp.guice.example01.service.Example01PetStoreService;
import org.tic.vertx.bp.guice.example01.service.impl.Example01PetStoreServiceImpl;

public class Example01Module extends AbstractModule {
    @Override
    protected void configure() {
        bind(Example01DogController.class).in(Singleton.class);
        bind(Example01PetStoreService.class).to(Example01PetStoreServiceImpl.class).in(Singleton.class);
        bind(Example01PetStoreDao.class).to(Example01PetStoreDaoImpl.class).in(Singleton.class);

        install(new RouteControllerModule());
    }
}
