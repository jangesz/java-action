package org.tic.vertx.examples.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebExample06RouterVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(WebExample06RouterVerticle.class);

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);

        /*
         * 精确匹配
         *
         * /api/product
         * /api/product/
         * /api/product//
         *
         * 以上都会进入到该处理器执行
         */
        router.route("/api/product").handler(this::apiProduct);

        /*
         * 基于路径前缀的路由
         */
        router.route("/api/product/*").handler(this::apiProductGet);

        /*
         * 捕捉路径参数
         */
        router.route("/api/user/:id").handler(this::userGetById);

        /*
         * 基于正则表达式的路由
         *
         * 匹配如下路由：
         *
         */
        router.routeWithRegex(".*foo").handler(this::foo);

        router.route().pathRegex("\\/([^\\/]+)\\/([^\\/]+)").handler(rc -> {
            String pp1 = rc.pathParam("param0");
            String pp2 = rc.pathParam("param1");

            logger.info("pp1 = " + pp1 + " , pp2 = " + pp2);

            rc.response().end();
        });

        /*
         * 基于 HTTP Method 的路由
         */
        router.route().method(HttpMethod.POST).path("/api/order").handler(rc -> {
            rc.response().end("/api/order request ok !");
        });

        // 在使用post接收消息体的时候，需要开启BodyHandler
        router.post().handler(BodyHandler.create());
        /*
         * 基于请求媒体类型（MIME types）的路由
         */
        router.route().path("/api/mime/example1").consumes("application/json").handler(rc -> {
            logger.info(rc.getBodyAsJson().toString());
            rc.response().end("request mime application/json ok!");
        });

        router.route().path("/api/mime/example2").handler(rc -> {
            logger.info("query params => " + rc.queryParams());
            logger.info("body data => " + rc.getBodyAsString());
            rc.response().end("request mime example2 ok !");
        });

        router.route().path("/api/exception/occured").handler(rc -> {
            logger.info("entering /api/exception router");
            throw new RuntimeException("what happened");
//            rc.response().end("entering /api/exception router");
        }).failureHandler(rc -> {
            rc.response().end("exception occured !");
        });

//        router.exceptionHandler(t -> {
//            logger.error(t.getCause().getMessage());
//        });


//        router.route().failureHandler()

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(9999, r -> {
                   if (r.succeeded()) {
                       logger.info("listen in port 9999 succeed !");
                   } else {
                       logger.warn("listen in port 9999 failed !");
                   }
                });
    }

    private void foo(RoutingContext context) {
        context.response().end("routeWithRegex(\".*foo\")");
    }

    private void userGetById(RoutingContext context) {
        logger.info("/api/user/:id request succeed ! user id = " + context.pathParam("id"));
        context.response().end();
    }

    private void apiProductGet(RoutingContext context) {
        logger.info("/api/product/* request succeed !");
        System.out.println(context.queryParams());
        System.out.println(context.queryParam("c"));
        System.out.println(context.queryParam("c") == null);
        if (context.queryParam("c") != null) {
            System.out.println(context.queryParam("c").equals(""));
        }
        context.response().end();
    }


    private void apiProduct(RoutingContext context) {
        logger.info("/api/product request succeed !");
        context.response().end("/api/product request succeed !");
    }




}
