package org.tic.vertx.examples.service.discovery.basic;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.EventBusService;

public class EventBusSimpleServiceDiscoveryVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

//        Record record = EventBusService.createRecord(
//                "some-eventbus-service", // The service name
//                "address", // the service address,
////                "examples.MyService", // the service interface as string
//                MyService.class,
//                new JsonObject()
//                        .put("some-metadata", "some value")
//        );

        Record record = new Record()
                .setType("eventbus-service-proxy")
                .setLocation(new JsonObject().put("endpoint", "the-service-address"))
                .setName("my-service")
                .setMetadata(new JsonObject().put("some-label", "some-value"));

        discovery.publish(record, ar -> {
            // ...
            if (ar.succeeded()) {
                System.out.println("service publish succeed !");
            } else {
                System.out.println("service publish failed !");
            }
        });

        discovery.getRecord(new JsonObject().put("name", "some-eventbus-service"), ar -> {
            System.out.println("ar.succeed = " + ar.succeeded());
            System.out.println("ar.result = " + ar.result());
            if (ar.succeeded() && ar.result() != null) {
                System.out.println(ar.result());
                // Retrieve the service reference
                ServiceReference reference = discovery.getReference(ar.result());
                // Retrieve the service object
                MyService service = reference.getAs(MyService.class);
                System.out.println(service);
                // Dont' forget to release the service
                reference.release();
            } else {
                System.out.println(ar.cause().getMessage());
            }
        });

    }
}
