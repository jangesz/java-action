package org.tic.vertx.examples.service.discovery;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.EventBusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tic.vertx.examples.service.discovery.common.MyEventBusService;

public class EventBusServiceConsumerVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(EventBusServiceConsumerVerticle.class);

    private static final String SERVICE_NAME = "product-service";

    @Override
    public void start() throws Exception {
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

//        EventBusService.get

        discovery.getRecord(new JsonObject().put("name", SERVICE_NAME), ar -> {
            if (ar.succeeded()) {
                ServiceReference reference = discovery.getReference(ar.result());

                MyEventBusService service = reference.getAs(MyEventBusService.class);
                service.add(6, 4, mar -> {
                    if (mar.succeeded()) {
                        logger.info("a + b = " + mar.result());
                    } else {
                        logger.error("a + b calc failed !" + mar.cause().getMessage());
                    }
                });

                // 释放
                reference.release();
            } else {
                logger.error("event bus service get failure !");
            }
        });
    }
}
