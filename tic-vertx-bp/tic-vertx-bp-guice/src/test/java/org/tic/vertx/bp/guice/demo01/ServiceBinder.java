package org.tic.vertx.bp.guice.demo01;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class ServiceBinder extends AbstractModule {

    @Provides @Singleton public AppConfig appConfig() {
        return new AppConfig();
    }

    @Override
    protected void configure() {

    }
}
