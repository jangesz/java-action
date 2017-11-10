package org.tic.vertx.examples.service.discovery;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.HttpEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpEndpointServiceProviderVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(HttpEndpointServiceProviderVerticle.class);

    private static final String API_NAME = "product-api";

    @Override
    public void start() throws Exception {
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

        Record record = HttpEndpoint.createRecord(API_NAME, "localhost", 9999, "/api/product");

        Router router = Router.router(vertx);
        router.get("/api/product", this::apiProduct);

        vertx.createHttpServer().requestHandler(router::accept)
                .listen(9999);

        discovery.publish(record, ar -> {
            if (ar.succeeded()) {
                logger.info(API_NAME + " http endpoint service provider published succeed !");
            } else {
                logger.error(API_NAME + " http endpoint service provider published failed !");
            }
        });
    }

    private void apiProduct() {

    }

}
