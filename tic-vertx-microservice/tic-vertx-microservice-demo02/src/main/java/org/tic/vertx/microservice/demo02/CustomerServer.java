package org.tic.vertx.microservice.demo02;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.ResponseContentTypeHandler;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.consul.ConsulServiceImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tic.vertx.microservice.demo02.client.AccountClient;
import org.tic.vertx.microservice.demo02.data.Customer;
import org.tic.vertx.microservice.demo02.data.CustomerRepository;

import java.util.List;

public class CustomerServer extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServer.class);

    public static void main(String[] args) throws Exception {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MongoVerticle());
        vertx.deployVerticle(new CustomerServer());
    }

    @Override
    public void start() throws Exception {
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);
        CustomerRepository repository = CustomerRepository.createProxy(vertx, "customer-service");

        Router router = Router.router(vertx);
        router.route("/customer/*").handler(ResponseContentTypeHandler.create());
        router.route(HttpMethod.POST, "/customer").handler(BodyHandler.create());
        router.get("/customer/:id").produces("application/json").handler(rc -> {
            repository.findById(rc.request().getParam("id"), res -> {
                Customer customer = res.result();
                logger.info("Found: {}", customer);
                new AccountClient(discovery).findCustomerAccounts(customer.getId(), res2 -> {
                    customer.setAccounts(res2.result());
                    rc.response().end(customer.toString());
                });
            });
        });
        router.get("/customer/name/:name").produces("application/json").handler(rc -> {
            repository.findByName(rc.request().getParam("name"), res -> {
                List<Customer> customers = res.result();
                logger.info("Found: {}", customers);
                rc.response().end(Json.encodePrettily(customers));
            });
        });
        router.get("/customer").produces("application/json").handler(rc -> {
            repository.findAll(res -> {
                List<Customer> customers = res.result();
                logger.info("Found all: {}", customers);
                rc.response().end(Json.encodePrettily(customers));
            });
        });
        router.post("/customer").produces("application/json").handler(rc -> {
            Customer c = Json.decodeValue(rc.getBodyAsString(), Customer.class);
            repository.save(c, res -> {
                Customer customer = res.result();
                logger.info("Created: {}", customer);
                rc.response().end(customer.toString());
            });
        });
        router.delete("/customer/:id").handler(rc -> {
            repository.remove(rc.request().getParam("id"), res -> {
                logger.info("Removed: {}", rc.request().getParam("id"));
                rc.response().setStatusCode(200);
            });
        });

        ConfigStoreOptions file = new ConfigStoreOptions().setType("file").setConfig(new JsonObject().put("path", "application.json"));
        ConfigRetriever retriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(file));
        retriever.getConfig(conf -> {
            JsonObject discoveryConfig = conf.result().getJsonObject("discovery");
            discovery.registerServiceImporter(new ConsulServiceImporter(), new JsonObject().put("host", discoveryConfig.getString("host")).put("port", discoveryConfig.getInteger("port")).put("scan-period", 2000));
            vertx.createHttpServer().requestHandler(router::accept).listen(conf.result().getInteger("port"));
        });

    }

}
