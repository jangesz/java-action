package org.tic.vertx.bp.guice.demo01.biz;

import com.google.inject.ImplementedBy;
import org.tic.vertx.bp.guice.demo01.biz.impl.OrderBizImpl;

@ImplementedBy(OrderBizImpl.class)
public interface OrderBiz {
}
