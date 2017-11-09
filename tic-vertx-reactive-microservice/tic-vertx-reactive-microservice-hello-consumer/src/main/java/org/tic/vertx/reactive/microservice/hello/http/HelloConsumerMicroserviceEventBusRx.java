package org.tic.vertx.reactive.microservice.hello.http;

import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.RxHelper;
import io.vertx.rxjava.core.eventbus.EventBus;
import io.vertx.rxjava.core.eventbus.Message;
import rx.Single;

import java.util.concurrent.TimeUnit;

public class HelloConsumerMicroserviceEventBusRx extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        vertx.createHttpServer()
            .requestHandler(
                req -> {
                    EventBus bus = vertx.eventBus();
                    Single<JsonObject> obs1 = bus.
                            <JsonObject>rxSend("hello", "Luke")
                            .subscribeOn(RxHelper.scheduler(vertx))
                            .timeout(3, TimeUnit.SECONDS)
                            .retry()
                            .map(Message::body);
                    Single<JsonObject> obs2 = bus.
                            <JsonObject>rxSend("hello", "Leia")
                            .subscribeOn(RxHelper.scheduler(vertx))
                            .timeout(3, TimeUnit.SECONDS)
                            .retry()
                            .map(Message::body);

                    Single.zip(obs1, obs2, (luke, leia) ->
                        new JsonObject()
                            .put("Luke", luke.getString("message")
                                + " from "
                                + luke.getString("serverd-by"))
                            .put("leia", leia.getString("message")
                                + " from "
                                + leia.getString("served_by"))
                    ).subscribe(
                        x -> req.response().end(x.encodePrettily()),
                        t -> {
                            t.printStackTrace();
                            req.response().setStatusCode(500).end(t.getMessage());
                        }
                    );
                }
            ).listen(4567);
    }
}
