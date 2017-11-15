package org.tic.vertx;

import io.vertx.core.Future;

public class VertxFutureTest {

    public static void main(String[] args) {
        Future<String> future = Future.future();
        Future<Void> future1 = Future.future();

        future1.setHandler(ar -> {
            if (ar.succeeded()) {
                System.out.println("11 " + ar.result());
            } else {
                System.out.println("11 e " + ar.cause().toString());
            }
        });

        future.compose(s -> {
            System.out.println("future do " + s);
            Future<String> fail = Future.future();
            fail.fail("qqq");
            return fail;
        }).compose(s -> {
            System.out.println("sss");
            future1.complete();
            return future1;
        }).setHandler(ar -> {
            if (ar.succeeded()) {
                System.out.println("set handler return succeed" + ar.result());
            } else {
                System.out.println("set handler return failed " + ar.cause().getMessage());
            }
        });

        // 调用FutureImpl的complete(T result)方法
        // tryComplete(result);
        // 调用handler.handle()方法
        // 当handler.handle()方法执行成功后，会执行compose的第一个参数
        // 如果执行失败，则任何一个compose都不会执行，需要setHandler来捕获异常
        future.complete("hahaha");
    }

}
