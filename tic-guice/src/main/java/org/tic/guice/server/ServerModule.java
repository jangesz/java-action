package org.tic.guice.server;

import com.google.inject.AbstractModule;
import org.tic.guice.server.impl.OrderServiceImpl;
import org.tic.guice.server.impl.PaymentServiceImpl;
import org.tic.guice.server.impl.PriceServiceImpl;

public class ServerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PriceService.class).to(PriceServiceImpl.class);
        bind(PaymentService.class).to(PaymentServiceImpl.class);
        bind(OrderService.class).to(OrderServiceImpl.class);
    }
}
