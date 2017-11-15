package org.tic.vertx.microservice.demo01;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.serviceproxy.ProxyHelper;
import org.tic.vertx.microservice.demo01.dao.AccountRepository;
import org.tic.vertx.microservice.demo01.dao.AccountRepositoryImpl;

public class MongoVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        ConfigStoreOptions file = new ConfigStoreOptions()
                .setType("file")
                .setConfig(new JsonObject().put("path", "application.json"));

        ConfigRetriever retriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(file));

        retriever.getConfig(conf -> {
            JsonObject datasourceConfig = conf.result().getJsonObject("datasource");
            JsonObject o = new JsonObject();
            o.put("host", datasourceConfig.getString("host"));
            o.put("port", datasourceConfig.getInteger("port"));
            o.put("db_name", datasourceConfig.getString("db_name"));
            o.put("username", datasourceConfig.getString("username"));
            o.put("password", datasourceConfig.getString("password"));
            final MongoClient client = MongoClient.createShared(vertx, o);
            final AccountRepository service = new AccountRepositoryImpl(client);
            ProxyHelper.registerService(AccountRepository.class, vertx, service, "account-service");
        });
    }
}
