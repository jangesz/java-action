package org.tic.vertx.bp.guice.demo01.action;

import org.tic.vertx.bp.guice.demo01.biz.OrderBiz;

import javax.inject.Inject;

public class OrderAction {

    private final OrderBiz orderBiz;

    @Inject
    public OrderAction(OrderBiz orderBiz) {
        this.orderBiz = orderBiz;
    }
}
