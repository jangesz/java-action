package org.tic.guice.example02;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Example02 {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new Example02Module());
        Cat cat = injector.getInstance(Cat.class);
        CatStore store = injector.getInstance(CatStore.class);

        System.out.println(cat);
        System.out.println(store);
    }

}
