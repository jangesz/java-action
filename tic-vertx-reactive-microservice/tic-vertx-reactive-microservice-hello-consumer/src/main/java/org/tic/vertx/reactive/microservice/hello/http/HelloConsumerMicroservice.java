package org.tic.vertx.reactive.microservice.hello.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;

public class HelloConsumerMicroservice extends AbstractVerticle {

    private WebClient client;

    @Override
    public void start() throws Exception {
        client = WebClient.create(vertx);

        Router router = Router.router(vertx);

        //这个写法，可以用下面的代替
//        router.get("/").handler(rc -> {invokeMyFirstMicroservice(rc);});

        router.get("/").handler(this::invokeMyFirstMicroservice);


        vertx.createHttpServer().requestHandler(router::accept).listen(4567);

    }

    private void invokeMyFirstMicroservice(RoutingContext rc) {
        HttpRequest<JsonObject> request = client
                .get(3456, "localhost", "/vert.x")
                .as(BodyCodec.jsonObject());

        request.send(ar -> {
            if (ar.failed()) {
                rc.fail(ar.cause());
            } else {
                rc.response().end(ar.result().body().encode());
            }
        });
    }


}
