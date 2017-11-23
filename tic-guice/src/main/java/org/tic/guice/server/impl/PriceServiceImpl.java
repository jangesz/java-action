package org.tic.guice.server.impl;

import org.tic.guice.server.PriceService;

import java.util.Arrays;
import java.util.List;

public class PriceServiceImpl implements PriceService {

    @Override
    public long getPrice(long orderId) {
        return 456L;
    }

    @Override
    public List<String> getSupportCurrencies() {
        return Arrays.asList("CNY", "USD", "EUR");
    }
}
