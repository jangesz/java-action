package org.tic.guice.server;

import com.google.inject.Guice;
import org.junit.Test;

public class OrderServiceTest {

    @Test
    public void testSendToPayment() {
        OrderService orderService = Guice.createInjector(new ServerModule())
                .getInstance(OrderService.class);
        orderService.sendToPayment(789L);
    }

}
