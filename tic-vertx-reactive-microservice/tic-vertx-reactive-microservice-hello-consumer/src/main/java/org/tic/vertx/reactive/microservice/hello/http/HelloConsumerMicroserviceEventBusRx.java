package org.tic.vertx.reactive.microservice.hello.http;

import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.RxHelper;
import io.vertx.rxjava.ext.web.client.HttpResponse;
import io.vertx.rxjava.ext.web.codec.BodyCodec;
import io.vertx.rxjava.servicediscovery.ServiceDiscovery;
import io.vertx.rxjava.servicediscovery.types.HttpEndpoint;

import java.util.concurrent.TimeUnit;

public class HelloConsumerMicroserviceEventBusRx extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        // We create an instance of service discovery
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

        // As we know we want to use an HTTP microservice, we can
        // retrieve a WebClient already configured for the service
        HttpEndpoint
            .rxGetWebClient(
                discovery,
                // This method is a filter to select the service
                rec -> rec.getName().endsWith("hello")
            )
            .flatMap(client ->
                // We have retrieved the WebClient, use it to call
                // the service
                // 在微服务中，一个服务调用另一个服务常常会出现问题，一个微服务架构需要是面向失败的（服务超时、服务异常等原因）
                // 所以，在异步调用服务的时候需要处理异常结果onErrorReturn

                // 加入超时和重试机制（超时和重试经常在一起使用）
                // 当超时发生，会触发重试机制
                client.get("/") // Invoke the service
                    .as(BodyCodec.jsonObject())
                    .rxSend()
                    // We neet to be sure to use the Vert.x event loop
                    .subscribeOn(RxHelper.scheduler(vertx))
                    // Configure the timeout, if no response, it publishes
                    // a failure in the Observable
                    .timeout(5, TimeUnit.SECONDS)
                    .retry(1)
                    // In case of success, extract the body
                    .map(HttpResponse::body)
                    // timeout or another exception
                    .onErrorReturn(t -> new JsonObject().put("err", t.getMessage()))
//                client.get("/").as(BodyCodec.string()).rxSend()
            ).subscribe(
                json -> System.out.println(json.toString())
            );
//            .subscribe(response -> System.out.println(response.body()));


//        HttpEndpoint
//            .rxGetWebClient(discovery, rec -> rec.getName().endsWith("hello"))
//            .flatMap(client ->
//                client.get("/")
//                    .as(BodyCodec.jsonObject())
//                    .rxSend()
//                    .map(HttpResponse::body)
//                    .onErrorReturn(t -> new JsonObject())
//            )
//            .subscribe(
//                json -> {/**/}
//            );

//        vertx.createHttpServer()
//            .requestHandler(
//                req -> {
//                    EventBus bus = vertx.eventBus();
//                    Single<JsonObject> obs1 = bus.
//                            <JsonObject>rxSend("hello", "Luke")
//                            .subscribeOn(RxHelper.scheduler(vertx))
//                            .timeout(3, TimeUnit.SECONDS)
//                            .retry()
//                            .map(Message::body);
//                    Single<JsonObject> obs2 = bus.
//                            <JsonObject>rxSend("hello", "Leia")
//                            .subscribeOn(RxHelper.scheduler(vertx))
//                            .timeout(3, TimeUnit.SECONDS)
//                            .retry()
//                            .map(Message::body);
//
//                    Single.zip(obs1, obs2, (luke, leia) ->
//                        new JsonObject()
//                            .put("Luke", luke.getString("message")
//                                + " from "
//                                + luke.getString("serverd-by"))
//                            .put("leia", leia.getString("message")
//                                + " from "
//                                + leia.getString("served_by"))
//                    ).subscribe(
//                        x -> req.response().end(x.encodePrettily()),
//                        t -> {
//                            t.printStackTrace();
//                            req.response().setStatusCode(500).end(t.getMessage());
//                        }
//                    );
//                }
//            ).listen(4567);
    }
}
