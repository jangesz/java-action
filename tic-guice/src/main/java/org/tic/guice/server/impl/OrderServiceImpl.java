package org.tic.guice.server.impl;

import org.tic.guice.server.OrderService;
import org.tic.guice.server.PaymentService;
import org.tic.guice.server.PriceService;
import org.tic.guice.server.SessionManager;

import javax.inject.Inject;

// 我们举例一个订单模块，一个订单业务可能需要一些依赖，比如PriceService......
public class OrderServiceImpl implements OrderService {
    // 不可以变的，加上final，来限定在构造函数过后就不能被改变了
    // 加了final，只能在构造函数赋值，其它地方赋值会报错
    // 所以加上final，就是保护了Guice为我们赋值的变量
    private final PriceService priceService;
    private final PaymentService paymentService;
    private final SessionManager sessionManager;

    // 可以变的
    private Long ordersPaid = 0L;

    @Inject
    public OrderServiceImpl(PriceService priceService, PaymentService paymentService, SessionManager sessionManager) {
        this.priceService = priceService;
        this.paymentService = paymentService;
        this.sessionManager = sessionManager;
    }

    public void sendToPayment(long orderId) {
        //TODO: send to payment
        long price = priceService.getPrice(orderId);

        paymentService.pay(orderId, price, sessionManager.getSessionId());

        ordersPaid += 1;
    }
}
