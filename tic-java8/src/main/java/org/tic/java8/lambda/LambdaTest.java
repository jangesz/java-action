package org.tic.java8.lambda;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LambdaTest {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        // 使用Lambda的书写格式
//        List<Callable<String>> list = Arrays.asList(() -> "Hello", () -> "World", () -> "test");

        // 传统的书写格式
        List<Callable<String>> list = Arrays.asList(
            new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "Hello";
            }
        }, new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "World";
            }
        }, new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "test";
            }
        });

        List<Future<String>> futures;
        try {
            futures = executor.invokeAll(list);

            for (Future future : futures) {
                System.out.println(future.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }

}
