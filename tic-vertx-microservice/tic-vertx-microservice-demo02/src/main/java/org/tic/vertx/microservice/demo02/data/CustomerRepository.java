package org.tic.vertx.microservice.demo02.data;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.mongo.MongoClient;

import java.util.List;

@ProxyGen
public interface CustomerRepository {

    @Fluent
    CustomerRepository save(Customer customer, Handler<AsyncResult<Customer>> resultHandler);

    @Fluent
    CustomerRepository findAll(Handler<AsyncResult<List<Customer>>> resultHandler);

    @Fluent
    CustomerRepository findById(String id, Handler<AsyncResult<Customer>> resultHandler);

    @Fluent
    CustomerRepository findByName(String name, Handler<AsyncResult<List<Customer>>> resultHandler);

    @Fluent
    CustomerRepository remove(String id, Handler<AsyncResult<Void>> resultHandler);

    static CustomerRepository createProxy(Vertx vertx, String address) {
        return new CustomerRepositoryVertxEBProxy(vertx, address);
    }

    static CustomerRepository create(MongoClient client) {
        return new CustomerRepositoryImpl(client);
    }

}