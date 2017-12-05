package org.tic.vertx.examples.web.ueditor.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.tic.vertx.examples.web.ueditor.service.UeditorService;
import org.tic.vertx.examples.web.ueditor.service.impl.UeditorServiceImpl;

public class UeditorModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(UeditorService.class).to(UeditorServiceImpl.class).in(Singleton.class);
    }
}
