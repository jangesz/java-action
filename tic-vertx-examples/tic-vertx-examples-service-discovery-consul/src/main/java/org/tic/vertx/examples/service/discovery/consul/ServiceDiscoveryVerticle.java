package org.tic.vertx.examples.service.discovery.consul;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.consul.ConsulServiceImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceDiscoveryVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(ServiceDiscoveryVerticle.class);

    @Override
    public void start() throws Exception {
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

        ConfigStoreOptions file = new ConfigStoreOptions().setType("file").setConfig(new JsonObject().put("path", "application.json"));
        ConfigRetriever retriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(file));
        retriever.getConfig(conf -> {
            JsonObject discoveryConfig = conf.result().getJsonObject("discovery");
            JsonObject configuration = new JsonObject()
                    .put("host", discoveryConfig.getString("host"))
                    .put("port", discoveryConfig.getInteger("port"))
                    .put("scan-period", 2000);

            discovery.registerServiceImporter(new ConsulServiceImporter(),configuration, ar -> {
                if (ar.succeeded()) {
                    discovery.getRecord(r -> r.getName().equals("account-service"), res -> {
                        Record record = res.result();
                        System.out.println(record.getLocation().toString());
                        System.out.println(record.getMetadata().toString());
                        logger.info("Result: {}", res.result().getType());
                        ServiceReference ref = discovery.getReference(res.result());
                        WebClient client = ref.getAs(WebClient.class);

                        String customerId = "5a0bd42a7c2a4f2018952592";
                        client.get(
                                record.getMetadata().getInteger("ServicePort"),
                                record.getMetadata().getString("ServiceAddress"),
                                "/account/customer/" + customerId
                        ).send(res2 -> {
                            if (res2.succeeded()) {
                                logger.info("Response: {}", res2.result().bodyAsString());
                            } else {
                                logger.error(res2.cause().getMessage());
                            }

                            //用完一定要release，否则提供服务端会报一个异常关闭连接的错误
                            ref.release();
                        });
                    });
                } else {
                    logger.error("can't register service importer !");
                }
            });
        });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(ServiceDiscoveryVerticle.class.getName());
    }

}
