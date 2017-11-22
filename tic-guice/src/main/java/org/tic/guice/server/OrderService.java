package org.tic.guice.server;

public interface OrderService {
    void sendToPayment(long orderId);
}
