package org.tic.guice.example02;

import javax.inject.Inject;

public class Cat {
    private String name;
    private Integer age;

    @Inject
    public Cat(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public void miao() {
        System.out.println(this.name + " " + this.age);
    }

}
