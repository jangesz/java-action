package org.tic.guice.example05;

import com.google.inject.AbstractModule;
import org.tic.guice.example05.impl.PriceServiceImpl;

public class Example05Module extends AbstractModule {
    @Override
    protected void configure() {
        bind(PriceService.class).to(PriceServiceImpl.class);
    }
}
