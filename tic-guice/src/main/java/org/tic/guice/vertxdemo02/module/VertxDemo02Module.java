package org.tic.guice.vertxdemo02.module;

import org.tic.guice.vertxdemocommon.module.BaseAbstractModule;

public class VertxDemo02Module extends BaseAbstractModule {

    private final String actionPackage = "org.tic.guice.vertxdemo02.action";

    @Override
    protected void configure() {
        bindActions(actionPackage);


    }
}
