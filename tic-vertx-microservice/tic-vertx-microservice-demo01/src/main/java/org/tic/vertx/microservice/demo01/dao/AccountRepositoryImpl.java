package org.tic.vertx.microservice.demo01.dao;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class AccountRepositoryImpl implements AccountRepository {

    private static final Logger logger = LoggerFactory.getLogger(AccountRepositoryImpl.class);

    private MongoClient client;

    public AccountRepositoryImpl(MongoClient client) {
        this.client = client;
    }

    @Override
    public AccountRepository save(Account account, Handler<AsyncResult<Account>> resultHandler) {
        JsonObject json = JsonObject.mapFrom(account);
        client.save(Account.DB_TABLE, json, res -> {
            if (res.succeeded()) {
                logger.info("Account created: {}", res.result());
                account.setId(res.result());
                resultHandler.handle(Future.succeededFuture(account));
            } else {
                logger.error("Account not created", res.cause());
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
        return this;
    }

    @Override
    public AccountRepository findAll(Handler<AsyncResult<List<Account>>> resultHandler) {
        client.find(Account.DB_TABLE, new JsonObject(), res -> {
            if (res.succeeded()) {
                List<Account> accounts = res.result()
                        .stream()
                        .map(it -> new Account(it.getString("_id"), it.getString("number"), it.getInteger("balance"), it.getString("customerId")))
                        .collect(Collectors.toList());
                resultHandler.handle(Future.succeededFuture(accounts));
            } else {
                logger.error("Account not found", res.cause());
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
        return this;
    }

    @Override
    public AccountRepository findById(String id, Handler<AsyncResult<Account>> resultHandler) {
        client.find(Account.DB_TABLE, new JsonObject().put("_id", id), res -> {
            if (res.succeeded()) {
                List<Account> accounts = res.result()
                        .stream()
                        .map(it -> new Account(it.getString("_id"), it.getString("number"), it.getInteger("balance"), it.getString("customerId")))
                        .collect(Collectors.toList());
                resultHandler.handle(Future.succeededFuture(accounts.get(0)));
            } else {
                logger.error("Account not found", res.cause());
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
        return this;
    }

    @Override
    public AccountRepository findByCustomer(String customerId, Handler<AsyncResult<List<Account>>> resultHandler) {
        client.find(Account.DB_TABLE, new JsonObject().put("customerId", customerId), res -> {
            if (res.succeeded()) {
                List<Account> accounts = res.result()
                        .stream()
                        .map(it -> new Account(it.getString("_id"), it.getString("number"), it.getInteger("balance"), it.getString("customerId")))
                        .collect(Collectors.toList());

                resultHandler.handle(Future.succeededFuture(accounts));
            } else {
                logger.error("Account not found", res.cause());
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
        return this;
    }

    @Override
    public AccountRepository remove(String id, Handler<AsyncResult<Account>> resultHandler) {
        client.removeDocument(Account.DB_TABLE, new JsonObject().put("_id", id), res -> {
            if (res.succeeded()) {
                resultHandler.handle(Future.succeededFuture());
            } else {
                logger.error("Account not found", res.cause());
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
        return this;
    }
}
