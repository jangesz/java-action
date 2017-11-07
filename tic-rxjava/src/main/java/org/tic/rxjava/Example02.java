package org.tic.rxjava;


import rx.Observable;

public class Example02 {

    public static void main(String[] args) {
        Observable.just("Hello, World!")
                .map(String::hashCode)
                .map(i -> Integer.toString(i))
                .subscribe(System.out::println);

        //等价于下面的操作
        String h = "Hello, World!";
        int i = h.hashCode();
        System.out.println(Integer.toString(i));

    }

}
