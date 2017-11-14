package org.tic.vertx.examples.service.discovery.basic;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.JDBCDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ServiceDiscoveryListenerVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(ServiceDiscoveryListenerVerticle.class);

    @Override
    public void start() throws Exception {
        ServiceDiscoveryOptions options = new ServiceDiscoveryOptions();
        options.setUsageAddress("vertx.discovery.usage.stat");
        options.setAnnounceAddress("vertx.discovery.announce.event");

        List<Record> announces = new ArrayList<>();
        vertx.eventBus().consumer(options.getAnnounceAddress(), msg -> {
            announces.add(new Record((JsonObject) msg.body()));
        });


        List<Record> usages = new ArrayList<>();
        vertx.eventBus().consumer(options.getUsageAddress(), msg -> {
            System.out.println("msg => " + msg.body().toString());
            usages.add(new Record((JsonObject) msg.body()));
        });

        vertx.setPeriodic(1000, h -> {
            System.out.println("announces as follows---------------");
            for (Record announce : announces) {
                System.out.println(announce.toJson());
            }

            System.out.println("usages as follows------------------");
            for (Record usage : usages) {
                System.out.println(usage.toJson());
            }
        });

        Record record = JDBCDataSource.createRecord("hikari-mysql-datasource",
                new JsonObject().put("url", "jdbc:mysql://182.61.39.59:3306/crux"),
                new JsonObject()
                        // 这里通过provider_class来指定使用哪个数据库连接池
                        .put("provider_class", "io.vertx.ext.jdbc.spi.impl.HikariCPDataSourceProvider")
                        .put("driverClassName", "com.mysql.jdbc.Driver")
                        .put("jdbcUrl", "jdbc:mysql://192.168.2.242:3306/crux?useUnicode=true&amp;characterEncoding=UTF-8")
                        .put("username", "root")
                        .put("password", "")
                        .put("autoCommit", false)
                        .put("connectionTimeut", 30000)
                        .put("idleTimeout", 600000)
                        .put("maxLifetime", 1800000)
                        .put("maximumPoolSize", 10)
        );

        ServiceDiscovery discovery = ServiceDiscovery.create(vertx, options);
        discovery.publish(record, ar -> {
            if (ar.succeeded()) {
                logger.info("publish jdbc datasource service succeed !");
            } else {
                logger.error("publish jdbc datasource service failed !");
            }
        });


        JDBCDataSource.getJDBCClient(discovery, new JsonObject().put("name", "hikari-mysql-datasource"), ar -> {
            if (ar.succeeded()) {
                JDBCClient client = ar.result();

                vertx.setPeriodic(3000, h -> {
                    client.getConnection( res -> {
                        if (res.succeeded()) {
                            SQLConnection connection = res.result();

                            connection.query("select * from todo", res2 -> {
                                if (res2.succeeded()) {
                                    System.out.println(res2.result().toJson());
                                } else {
                                    logger.warn("can't select * from todo !");
                                }
                                connection.close();
                            });
                        } else {
                            logger.warn("can't get connection !");
                        }
                    });
                });
            } else {
                logger.error("can't get jdbc client !");
            }
        });

    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(ServiceDiscoveryListenerVerticle.class.getName());
    }

}
