package org.tic.guice.example02;

import javax.inject.Inject;

public class CatStore {
    @Inject private Cat cat;

    public void miao() {
        System.out.println(cat);
    }

}
