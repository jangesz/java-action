package org.tic.vertx.examples.service.discovery.basic;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.JDBCDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDBCDatasourceSimpleServiceDiscoveryVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(JDBCDatasourceSimpleServiceDiscoveryVerticle.class);

    @Override
    public void start() throws Exception {
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

//        JsonObject conf = new JsonObject().put("driverClass", "org.hsqldb.jdbcDriver");
        JsonObject conf = new JsonObject()
                .put("driver_class", "com.mysql.jdbc.Driver")
                .put("user", "root")
                .put("password", "minorfish")
                .put("max_pool_size", 10);

//        Record record = JDBCDataSource.createRecord("some-hsql-db",
//                new JsonObject().put("url", "jdbc:hsqldb:file:target/dumb-db;shutdown=true"),
//                new JsonObject().put("database", "some-raw-data"));

        Record record = JDBCDataSource.createRecord("some-mysql-db",
                new JsonObject().put("url", "jdbc:mysql://192.168.2.242:3306/crux"),
                new JsonObject().put("database", "some-raw-data"));

        discovery.publish(record, ar -> {
            if (ar.succeeded()) {
                logger.info("publish jdbc datasource service succeed !");
            } else {
                logger.error("publish jdbc datasource service failed !");
            }
        });

        vertx.setTimer(3000, h -> {
            discovery.getRecord(new JsonObject().put("name", "some-mysql-db"), ar -> {
                if (ar.succeeded()) {
                    ServiceReference reference = discovery.getReferenceWithConfiguration(ar.result(), conf);
                    JDBCClient client = reference.get();
                    client.getConnection( res -> {
                        if (res.succeeded()) {
                            SQLConnection connection = res.result();

                            connection.query("select * from todo", res2 -> {
                                if (res2.succeeded()) {
                                    System.out.println(res2.result().toJson());
                                } else {
                                    logger.warn("can't select * from todo !");
                                }
                            });
                        } else {
                            logger.warn("can't get connection !");
                        }
                    });
                } else {
                    logger.error("can't get jdbc datasource service !");
                }
            });
        });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(JDBCDatasourceSimpleServiceDiscoveryVerticle.class.getName());
    }

}
