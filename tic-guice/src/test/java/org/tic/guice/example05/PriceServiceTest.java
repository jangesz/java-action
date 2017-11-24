package org.tic.guice.example05;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.tic.guice.example05.impl.PriceServiceImpl;

public class PriceServiceTest {

    @Before public void setUp() {
        // 1. 会抛出异常
//        Guice.createInjector(new Example05Module()).injectMembers(this);


        // 2. 通过连接绑定来实现覆盖原来抛出异常的getPrice方法
        Guice.createInjector(new Example05Module(), new AbstractModule() {
            @Override
            protected void configure() {
                bind(PriceServiceImpl.class).to(PriceServiceMock.class);
            }
        }).injectMembers(this);


        // 3. 
    }


    @Inject private PriceService priceService;

    @Test
    public void testGetPrice() {
        priceService.getPrice();
    }
}

class PriceServiceMock extends PriceServiceImpl {
    @Override
    public double getPrice() {
        return 123.00;
    }
}
