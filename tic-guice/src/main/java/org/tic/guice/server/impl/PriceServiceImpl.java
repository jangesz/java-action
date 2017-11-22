package org.tic.guice.server.impl;

import org.tic.guice.server.PriceService;

public class PriceServiceImpl implements PriceService {
    @Override
    public long getPrice(long orderId) {
        return 456L;
    }
}
