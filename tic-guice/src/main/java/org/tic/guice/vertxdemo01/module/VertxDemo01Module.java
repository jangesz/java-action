package org.tic.guice.vertxdemo01.module;

import org.tic.guice.vertxdemocommon.module.BaseAbstractModule;

public class VertxDemo01Module extends BaseAbstractModule {

    private static final String actionPkg = "org.tic.guice.vertxdemo01.action";

    @Override
    protected void configure() {
        bindActions(actionPkg);
    }
}
