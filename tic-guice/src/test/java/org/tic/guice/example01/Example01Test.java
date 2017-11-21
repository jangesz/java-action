package org.tic.guice.example01;

import com.google.inject.*;
import org.junit.Test;

public class Example01Test {

    @Test
    public void test01() {
        Injector injector = Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {
                binder.bind(Dog.class).to(BlackDog.class).in(Singleton.class);
            }
        });

        System.out.println(injector.getInstance(Dog.class));


        test01_1();
    }

    private void test01_1() {
        Injector injector = Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {
                binder.bind(Dog.class).to(BlackDog.class).in(Singleton.class);
            }
        });

        System.out.println(injector.getInstance(Dog.class));
    }


}
