package org.tic.vertx.bp.guice.example01.controller;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.tic.vertx.bp.guice.example01.entity.Dog;
import org.tic.vertx.bp.guice.example01.service.Example01PetStoreService;

import javax.inject.Inject;

public class Example01PetStoreController {

    private final Vertx vertx;
    private final Example01PetStoreService example01PetStoreService;
    private final Router router;

    @Inject
    public Example01PetStoreController(Vertx vertx, Example01PetStoreService example01PetStoreService) {
        this.vertx = vertx;
        this.example01PetStoreService = example01PetStoreService;

        //初始化路由
        router = Router.router(vertx);

        router.get("/dog").handler(this::getPetStoreDog);
    }

    private void getPetStoreDog(RoutingContext context) {
        String name = context.request().getParam("name");
        Dog dog = this.example01PetStoreService.findDogByName(name);
        context.response().end(dog.toString());
    }

    public Router getRouter() {
        return router;
    }

//    @Override
//    public String mountPoint() {
//        return "/petstore";
//    }
//
//    @Override
//    public Router router(Vertx vertx) {
//        Router router = Router.router(vertx);
//        router.get("/one").handler(ctx -> ctx.response().end("One OK"));
//        router.get("/dog").handler(this::getPetStoreDog);
//        return router;
//    }
}
