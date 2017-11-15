package org.tic.vertx.microservice.demo01;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.ResponseContentTypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tic.vertx.microservice.demo01.dao.Account;
import org.tic.vertx.microservice.demo01.dao.AccountRepository;

import java.util.List;

public class AccountServer extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(AccountServer.class);

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MongoVerticle());
        vertx.deployVerticle(new AccountServer());
    }

    @Override
    public void start() throws Exception {
        AccountRepository repository = AccountRepository.createProxy(vertx, "account-service");

        Router router = Router.router(vertx);

        router.route("/account/*").handler(ResponseContentTypeHandler.create());
        router.route(HttpMethod.POST, "/account").handler(BodyHandler.create());
        router.get("/account/:id").produces("application/json").handler(rc -> {
            repository.findById(rc.request().getParam("id"), res -> {
                if (res.succeeded()) {
                    Account account = res.result();
                    logger.info("Found: {}", account);
                    rc.response().end(account.toString());
                } else {
                    rc.response().end(res.cause().getMessage());
                }
            });
        });

        router.get("/account/customer/:customer").produces("application/json").handler(rc -> {
            repository.findByCustomer(rc.request().getParam("customer"), res -> {
                List<Account> accounts = res.result();
                logger.info("Found; {}", accounts);
                rc.response().end(Json.encodePrettily(accounts));
            });
        });

        router.get("/account").produces("application/json").handler(rc -> {
            repository.findAll(res -> {
                List<Account> acconts = res.result();
                logger.info("Found all: {}", acconts);
                rc.response().end(Json.encodePrettily(acconts));
            });
        });

        router.post("/account").produces("application/json").handler(rc -> {
            Account a = Json.decodeValue(rc.getBodyAsString(), Account.class);
            repository.save(a, res -> {
                if (res.succeeded()) {
                    Account account = res.result();
                    logger.info("Created: {}", account);
                    rc.response().end(account.toString());
                } else {
                    logger.error("Created failed: {}", res.cause().getMessage());
                    rc.response().end(res.cause().getMessage());
                }
            });
        });

        router.delete("/account/:id").produces("application/json").handler(rc -> {
            repository.remove(rc.request().getParam("id"), res -> {
                logger.info("Removed: {}", rc.request().getParam("id"));
                rc.response().end("removed ok!");
            });
        });

        WebClient client = WebClient.create(vertx);
        ConfigStoreOptions file = new ConfigStoreOptions()
                .setType("file")
                .setConfig(new JsonObject().put("path", "application.json"));
        ConfigRetriever retriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(file));
        retriever.getConfig(conf -> {
            JsonObject discoveryConfig = conf.result().getJsonObject("discovery");
            vertx.createHttpServer().requestHandler(router::accept).listen(conf.result().getInteger("port"));
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
}
