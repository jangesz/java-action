package org.tic.vertx.bp.guice.example01;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import org.tic.vertx.bp.guice.example01.controller.Example01PetStoreController;
import org.tic.vertx.bp.guice.example01.controller.RouteController;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

public class Example01Verticle extends AbstractVerticle {

    private final Example01PetStoreController example01PetStoreController;
    private final RouteController routeController;

    @Inject
    public Example01Verticle(Example01PetStoreController example01PetStoreController, RouteController routeController;) {
        this.example01PetStoreController = example01PetStoreController;
        this.routeController = routeController;
    }

    @Override
    public void start(Future<Void> future) throws Exception {
        int PORT = 9999;
        Router petStoreRouter = example01PetStoreController.getRouter();

        Router mainRouter = Router.router(vertx);

        mainRouter.mountSubRouter("/petstore", petStoreRouter);


        mainRouter.route().consumes("application/json");
        mainRouter.route().produces("application/json");

        Set<String> allowHeaders = getAllowedHeaders();
        Set<HttpMethod> allowMethods = getAllowedMethods();
        mainRouter.route().handler(BodyHandler.create());
        mainRouter.route().handler(CorsHandler.create("*")
                .allowedHeaders(allowHeaders)
                .allowedMethods(allowMethods));

        vertx.createHttpServer()
            .requestHandler(mainRouter::accept)
            .listen(PORT, res -> {
                if(res.succeeded()){
                    System.out.println("Server listening on port " + PORT);
                    future.complete();
                }
                else{
                    System.out.println("Failed to launch server");
                    future.fail(res.cause());
                }
            });
    }

    private Set<String> getAllowedHeaders(){
        Set<String> allowHeaders = new HashSet<>();
        allowHeaders.add("x-requested-with");
        allowHeaders.add("Access-Control-Allow-Origin");
        allowHeaders.add("origin");
        allowHeaders.add("Content-Type");
        allowHeaders.add("accept");
        return allowHeaders;
    }

    private Set<HttpMethod> getAllowedMethods(){
        Set<HttpMethod> allowMethods = new HashSet<>();
        allowMethods.add(HttpMethod.GET);
        allowMethods.add(HttpMethod.POST);
        allowMethods.add(HttpMethod.DELETE);
        allowMethods.add(HttpMethod.PATCH);
        return allowMethods;
    }
}
