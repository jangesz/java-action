package org.tic.vertx.reactive.microservice.hello.http;

import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.servicediscovery.ServiceDiscovery;
import io.vertx.rxjava.servicediscovery.types.HttpEndpoint;
import io.vertx.servicediscovery.Record;

public class HttpServiceProviderWithCircuitBreaker extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);
        vertx.createHttpServer()
            .requestHandler(req -> {
                double chaos = Math.random();
                System.out.println("chaos -> " + chaos);
                // 为了测试熔断器机制，这里生成一个随机数，如果随机数大于0.6，则延迟返回结果给调用方，
                // 这里设置延迟3s，因为服务请求方的熔断器超时设置成2s
                // 这里超时返回的信息不回被服务端接收到（i am delayed, i am sorry!）
                if (chaos > 0.6) {
                    vertx.setTimer(3000, h -> req
                            .response()
                            .end(Buffer.buffer(
                                    new JsonObject().put("hello", "i am delayed, i am sorry!").toString())
                            )
                    );
                }else {
                    req.response().end(Buffer.buffer(new JsonObject().put("hello", "my name is vert.x").toString()));
                }
            })
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
