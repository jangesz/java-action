package org.tic.guice.example02;

import com.google.inject.AbstractModule;

public class Example02Module extends AbstractModule {
    @Override
    protected void configure() {
        bind(String.class).toInstance("Chin");
        bind(Integer.class).toInstance(11);
    }
}
