package org.tic.vertx.examples.web;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class Launcher {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        // --------------例子--------------
        // vertx.deployVerticle(WebRouterExample01Verticle.class.getName());

        // --------------例子--------------
        // vertx.deployVerticle(WebRouterNextExample02Verticle.class.getName());

        // --------------例子--------------
        // 使用阻塞式处理器，通过Thread.sleep来测试，虽然我们使用了Thread.sleep，但是由于我们处理的时候使用
        // 的是blockingHandler，Vert.x会使用Work Verticle线程来处理，随意并没有阻塞EventLoop线程，这样做
        // 是没有问题的！
        //
        // 对比下面的例子，我们在EventLoop中阻塞线程，会有警告，这是黄金法则，不要阻塞EventLoop线程
        // vertx.deployVerticle(WebRouterBlockingExample03Verticle.class.getName());

        // --------------例子--------------
        // 由于阻塞了EventLoop线程，所以会有警告，检查EventLoop线程是否被阻塞的类是BlockedThreadChecker，如下：
        // 十一月 10, 2017 9:48:59 下午 io.vertx.core.impl.BlockedThreadChecker
        // 警告: Thread Thread[vert.x-eventloop-thread-0,5,main] has been blocked for 2178 ms, time limit is 2000
        // vertx.deployVerticle(WebRouterBlockingEventLoopExample04Verticle.class.getName());

        // --------------例子--------------
         vertx.deployVerticle(WebExample06RouterVerticle.class.getName());


//        JsonObject.mapFrom()


    }

}
