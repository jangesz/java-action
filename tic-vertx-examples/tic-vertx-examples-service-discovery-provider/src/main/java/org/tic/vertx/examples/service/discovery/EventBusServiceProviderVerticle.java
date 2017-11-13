package org.tic.vertx.examples.service.discovery;

import io.vertx.core.AbstractVerticle;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.EventBusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tic.vertx.examples.service.discovery.common.MyEventBusService;

public class EventBusServiceProviderVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(EventBusServiceProviderVerticle.class);

    private static final String SERVICE_NAME = "product-service";

    @Override
    public void start() throws Exception {

        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

        Record record = EventBusService.createRecord(
            SERVICE_NAME,
            "address",
            MyEventBusService.class.getName()
        );

        discovery.publish(record, ar -> {
            if (ar.succeeded()) {
                logger.info("publish record succeed !");
            } else {
                logger.error("publish record failed !");
            }
        });

    }
}
