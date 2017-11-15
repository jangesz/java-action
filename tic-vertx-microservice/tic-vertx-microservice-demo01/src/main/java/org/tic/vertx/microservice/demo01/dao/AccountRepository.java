package org.tic.vertx.microservice.demo01.dao;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.mongo.MongoClient;

import java.util.List;

@ProxyGen
public interface AccountRepository {

    @Fluent
    AccountRepository save(Account account, Handler<AsyncResult<Account>> resultHandler);

    @Fluent
    AccountRepository findAll(Handler<AsyncResult<List<Account>>> resultHandler);

    @Fluent
    AccountRepository findById(String id, Handler<AsyncResult<Account>> resultHandler);

    @Fluent
    AccountRepository findByCustomer(String customerId, Handler<AsyncResult<List<Account>>> resultHandler);

    @Fluent
    AccountRepository remove(String id, Handler<AsyncResult<Account>> resultHandler);

    static AccountRepository createProxy(Vertx vertx, String address) {
        return new AccountRepositoryVertxEBProxy(vertx, address);
    }

    static AccountRepository create(MongoClient client) {
        return new AccountRepositoryImpl(client);
    }

}
