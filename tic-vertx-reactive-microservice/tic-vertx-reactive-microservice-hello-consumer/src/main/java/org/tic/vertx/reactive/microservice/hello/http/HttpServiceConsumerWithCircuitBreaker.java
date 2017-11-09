package org.tic.vertx.reactive.microservice.hello.http;

import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.circuitbreaker.CircuitBreaker;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.ext.web.client.HttpResponse;
import io.vertx.rxjava.ext.web.codec.BodyCodec;
import io.vertx.rxjava.servicediscovery.ServiceDiscovery;
import io.vertx.rxjava.servicediscovery.types.HttpEndpoint;

public class HttpServiceConsumerWithCircuitBreaker extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        CircuitBreakerOptions options = new CircuitBreakerOptions()
            // Call the fallback on failures
            // 设置当出现请求超时时是否要开启处理
            .setFallbackOnFailure(true)
            // Set the operation timeout
            // 设置超时时间
            .setTimeout(2000)
            // Number of failures before switching to the 'open' state
            // 设置失败多少次后，打开熔断器
            .setMaxFailures(3)
            // Time before attemptting to reset the circuit breaker
            // 设置熔断器状态重置时间，也就是从OPEN状态到HALF_OPEN状态的检查时间
            .setResetTimeout(5000);
        CircuitBreaker circuit = CircuitBreaker.create("my-circuit", vertx, options);

        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

        // 每隔3秒发送一次请求
        vertx.setPeriodic(3000, h-> {
            //每次请求的时候打印一下熔断器的当前状态
            System.out.println("circuit breaker's state = " + circuit.state());

            HttpEndpoint.rxGetWebClient(discovery, rec -> rec.getName().endsWith("hello"))
                    .flatMap(
                            client -> circuit.rxExecuteCommandWithFallback(
                                    future -> client.get("/")
                                            .as(BodyCodec.jsonObject())
                                            .rxSend()
                                            .map(HttpResponse::body)
                                            .subscribe(future::complete, future::fail),
                                    // 如果服务请求超时，会返回operation timeout，当然这里我们可以自定义返回超时信息
                                    fallback -> new JsonObject().put("message", fallback.getMessage())
                            )
                    ).subscribe(
                    json -> System.out.println(json.encode())
            );
        });

//        HttpEndpoint.rxGetWebClient(discovery, rec -> rec.getName().endsWith("hello"))
//            .flatMap(
//                client -> circuit.rxExecuteCommandWithFallback(
//                    future -> client.get("/")
//                            .rxSend()
//                            .map(HttpResponse::body)
//                            .subscribe(future::complete, future::fail),
//                    fallback -> new JsonObject().put("message", fallback.getMessage())
//                )
//            ).subscribe(
//                json -> System.out.println(json.encode())
//            );


    }
}
