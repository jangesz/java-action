package org.tic.guice.server;

import java.util.List;

public interface PriceService {
    long getPrice(long orderId);

    List<String> getSupportCurrencies();
}
