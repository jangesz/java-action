package org.tic.guice.example05;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.util.Modules;
import org.junit.Before;
import org.junit.Test;
import org.tic.guice.example05.impl.PriceServiceImpl;
import org.tic.guice.server.ServerModule;

public class PriceServiceTest {

    @Before public void setUp() {
        // 1. 会抛出异常
//        Guice.createInjector(new Example05Module()).injectMembers(this);


        // 2. 通过连接绑定来实现覆盖原来抛出异常的getPrice方法
        // 通过PriceServiceImpl的连接绑定来实现有一个缺陷，如果PriceServiceImpl有变动了，这里就失效了
        // 所以最好的方法是，通过PriceService接口来实现绑定，如下3.
//        Guice.createInjector(new Example05Module(), new AbstractModule() {
//            @Override
//            protected void configure() {
//                bind(PriceServiceImpl.class).to(PriceServiceMock.class);
//            }
//        }).injectMembers(this);


        // 3. 抛出异常，意思是PriceService已经在Example05Module中有了绑定(PriceServiceImpl)，这里又要绑定给PriceServiceMock。
        // 出现这种应用场景，我们可以通过覆盖的方法来把现在的绑定覆盖之前的绑定，如4.操作
//        Guice.createInjector(new Example05Module(), new AbstractModule() {
//            @Override
//            protected void configure() {
//                bind(PriceService.class).to(PriceServiceMock.class);
//            }
//        }).injectMembers(this);


        // 4. 通过Modules.override(原来模块).with(现在模块)来实现绑定的覆盖
        // 这里override的是binding的语句(bing().to())
        Guice.createInjector(Modules.override(new Example05Module()).with(new AbstractModule() {
            @Override
            protected void configure() {
                bind(PriceService.class).to(PriceServiceMock.class);
            }
        })).injectMembers(this);

    }


    @Inject private PriceService priceService;

    @Test
    public void testGetPrice() {
        double price = priceService.getPrice();
        System.out.println(price);
    }
}

class PriceServiceMock extends PriceServiceImpl {
    @Override
    public double getPrice() {
        return 123.00;
    }
}
