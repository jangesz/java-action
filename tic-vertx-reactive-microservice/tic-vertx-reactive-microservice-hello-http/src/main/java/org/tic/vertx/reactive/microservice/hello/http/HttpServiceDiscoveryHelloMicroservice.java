package org.tic.vertx.reactive.microservice.hello.http;

import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.servicediscovery.ServiceDiscovery;
import io.vertx.rxjava.servicediscovery.types.HttpEndpoint;
import io.vertx.servicediscovery.Record;

public class HttpServiceDiscoveryHelloMicroservice extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

        vertx.createHttpServer()
//            .requestHandler(req -> req.response().end("hello"))
            .requestHandler(req -> req.response().end(Buffer.buffer(new JsonObject().put("hello", "my name is vert.x").toString())))
            .rxListen(9999)
            .flatMap(
                server -> {
                    Record record = HttpEndpoint.createRecord("hello", "localhost", server.actualPort(), "/");
                    return discovery.rxPublish(record);
                }
            )
            .subscribe(rec -> System.out.println("Service published!"));
    }
}
