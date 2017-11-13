package org.tic.vertx.examples.service.discovery;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.HttpEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpEndpointServiceConsumerVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(HttpEndpointServiceConsumerVerticle.class);

    private static final String API_NAME = "product-api";

    @Override
    public void start() throws Exception {
//        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);
//        discovery.getRecord(r -> r.getName().equals(API_NAME), ar -> {
//            if (ar.succeeded()) {
//                logger.info("consumer get record by name " + API_NAME + " succeed !");
//                Record record = ar.result();
//                ServiceReference reference = discovery.getReference(record);
//                HttpClient client = reference.getAs(HttpClient.class);
//                logger.info("record = " + record);
//                logger.info("service reference = " + record);
//                logger.info("http client = " + client);
//                client.getNow("/api/product", response -> {
//                    logger.info("get /api/product, response => " + response.bodyHandler(Buffer::toString));
//
//                    // 别忘了释放引用
//                    reference.release();
//                });
//            } else {
//                logger.error("consumer get record by name " + API_NAME + " failed !");
//            }
//        });

        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);
        HttpEndpoint.getWebClient(discovery, new JsonObject().put("name", API_NAME), ar -> {
            if (ar.succeeded()) {
                logger.info("consumer get record by name " + API_NAME + " succeed !");

                WebClient client = ar.result();

                client.get("/api/product")
                    .send(response -> {
                        logger.info(response.result().body().toString());
                        ServiceDiscovery.releaseServiceObject(discovery, client);
                    });
            } else {
                logger.error("consumer get record by name " + API_NAME + " failed !");
            }
        });
    }
}
