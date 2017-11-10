package org.tic.vertx.examples.service.discovery;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.HttpEndpoint;

public class SimpleServiceProviderVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

        Record record = new Record()
                .setType("eventbus-service-proxy")
                .setLocation(new JsonObject().put("endpoint", "the-service-address"))
                .setName("my-service")
                .setMetadata(new JsonObject().put("some-label", "some-value"));

        discovery.publish(record, ar -> {
            if (ar.succeeded()) {
                // publication succeed
                Record publishedRecord = ar.result();
            } else {
                // publication failed
            }
        });

        // Record creation from a type
//        record = HttpEndpoint.createRecord()

        // Customize the configuration
//        discovery = ServiceDiscovery.create(vertx);


    }
}
