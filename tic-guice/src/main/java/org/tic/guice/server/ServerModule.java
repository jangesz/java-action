package org.tic.guice.server;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import org.tic.guice.server.impl.OrderServiceImpl;
import org.tic.guice.server.impl.PaymentServiceImpl;
import org.tic.guice.server.impl.PriceServiceImpl;

import java.util.List;

public class ServerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PriceService.class).to(PriceServiceImpl.class);
        bind(PaymentService.class).to(PaymentServiceImpl.class);
        bind(OrderService.class).to(OrderServiceImpl.class);

        // 命名绑定
        // 用这种方式需要注意的是，Guice在第一次绑定后，绑定的值就不变了
        // 所以如果需要每次绑定的值是动态的话，需要用Provider模式来绑定
        bind(Long.class).annotatedWith(SessionId.class).toInstance(System.currentTimeMillis());
        bind(Long.class).annotatedWith(Names.named("appId")).toInstance(456L);
    }

    @Provides @SessionId Long generateSessionId() {
        return System.currentTimeMillis();
    }

    @Provides List<String> getSupportCurrencies(PriceService priceService) {
        return priceService.getSupportCurrencies();
    }
}
