package org.tic.vertx.microservice.demo02.client;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tic.vertx.microservice.demo02.data.Account;

import java.util.List;
import java.util.stream.Collectors;

public class AccountClient {
    private static final Logger logger = LoggerFactory.getLogger(AccountClient.class);

    private ServiceDiscovery discovery;

    public AccountClient(ServiceDiscovery discovery) {
        this.discovery = discovery;
    }

    public AccountClient findCustomerAccounts(String customerId, Handler<AsyncResult<List<Account>>> resultHandler) {
        discovery.getRecord(r -> r.getName().equals("account-service"), res -> {
            Record record = res.result();
            System.out.println(record.getLocation().toString());
            logger.info("Result: {}", res.result().getType());
            ServiceReference ref = discovery.getReferenceWithConfiguration(record, new JsonObject().put("host", record.getLocation().getString("Address")).put("port", record.getLocation().getString("Port")));
//            ServiceReference ref = discovery.getReference(res.result());
            WebClient client = ref.getAs(WebClient.class);
            client.get("/account/customer/" + customerId).send(res2 -> {
                if (res2.succeeded()) {
                    logger.info("Response: {}", res2.result().bodyAsString());
                    List<Account> accounts = res2.result().bodyAsJsonArray().stream().map(it -> Json.decodeValue(it.toString(), Account.class)).collect(Collectors.toList());
                    resultHandler.handle(Future.succeededFuture(accounts));
                } else {
                    logger.error(res2.cause().getMessage());
                }
            });
        });
        return this;
    }

}
