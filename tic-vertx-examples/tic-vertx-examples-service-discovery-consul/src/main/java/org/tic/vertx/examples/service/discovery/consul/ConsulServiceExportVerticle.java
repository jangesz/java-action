package org.tic.vertx.examples.service.discovery.consul;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsulServiceExportVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(ConsulServiceExportVerticle.class);

    @Override
    public void start() throws Exception {
        WebClient client = WebClient.create(vertx);
        ConfigStoreOptions file = new ConfigStoreOptions()
                .setType("file")
                .setConfig(new JsonObject().put("path", "application.json"));
        ConfigRetriever retriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(file));
        retriever.getConfig(conf -> {
            JsonObject discoveryConfig = conf.result().getJsonObject("discovery");
            JsonObject json = new JsonObject()
                    .put("ID", "account-service-1")
                    .put("Name", "account-service")
                    .put("Address", "127.0.0.1")
                    .put("Port", 2222)
                    .put("Tags", new JsonArray().add("http-endpoint"));

            client.put(
                    discoveryConfig.getInteger("port"),
                    discoveryConfig.getString("host"),
                    "/v1/agent/service/register")
                    .sendJsonObject(json, res -> {
                        logger.info("Consul registration status: {}", res.result().statusCode());
                    });
        });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(ConsulServiceExportVerticle.class.getName());
    }

}
