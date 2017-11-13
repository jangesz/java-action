package org.tic.vertx.examples.service.discovery.basic;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.EventBusService;
import io.vertx.serviceproxy.ServiceBinder;

public class EventBusServiceDiscoveryExample extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

        ServiceBinder binder = new ServiceBinder(vertx);
        binder.setAddress("my-address").register(MathService.class, MathService.create(vertx));
        vertx.setTimer(3000, h -> {
//            MathService service = MathService.createProxy(vertx, "my-address");
//            service.add(7, 8, mar -> {
//                if (mar.succeeded()) {
//                    System.out.println("result => " + mar.result());
//                } else {
//                    System.out.println("result => " + mar.cause().getMessage());
//                }
//            });
//            EventBusService.getServiceProxyWithJsonFilter(discovery, new JsonObject().put("address", "my-address"), MathService.class, ar -> {
//                if (ar.succeeded()) {
//                    System.out.println(ar.result());
//                    MathService service = ar.result();
//                    service.add(7, 8, mar -> {
//                        if (mar.succeeded()) {
//                            System.out.println("result => " + mar.result());
//                        } else {
//                            System.out.println("result => " + mar.cause().getMessage());
//                        }
//                    });
//                } else {
//                    System.out.println("can't find MathService !" + ar.cause().getMessage());
//                }
//            });


            discovery.getRecord(new JsonObject().put("name", "my-eventbus-service"), ar -> {
                if (ar.succeeded() && ar.result() != null) {
                    ServiceReference reference = discovery.getReference(ar.result());
                    MathService service = reference.getAs(MathService.class);
                    service.add(7, 8, mar -> {
                        if (mar.succeeded()) {
                            System.out.println("result => " + mar.result());
//                            reference.release();
                        } else {
                            System.out.println("result => " + mar.cause().getMessage());
                        }
                    });
//                    service.add(7, 8);
//                    reference.release();
                } else {
                    System.out.println("can't find MathService" + ar.cause().getMessage());
                }
            });
        });



    }
}
