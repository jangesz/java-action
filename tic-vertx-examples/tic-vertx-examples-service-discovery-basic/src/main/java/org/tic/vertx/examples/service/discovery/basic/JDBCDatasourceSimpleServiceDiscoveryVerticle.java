package org.tic.vertx.examples.service.discovery.basic;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.JDBCDataSource;

public class JDBCDatasourceSimpleServiceDiscoveryVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);


//        JsonObject config = new JsonObject()
//                .put("url", "jdbc:mysql://192.168.2.242:3306/maanshan")
//                .put("user", "root")
//                .put("password", "minorfish")
//                .put("driver_class", "com.mysql.jdbc.Driver")
//                .put("max_pool_size", 10);
//
//        JDBCClient jdbcClient = JDBCClient.createShared(vertx, config);
//        System.out.println(jdbcClient);

        Record record = JDBCDataSource.createRecord(
                "some-data-source-service",
                new JsonObject().put("url", "jdbc:mysql://192.168.2.242:3306/maanshan"),
                new JsonObject().put("some-metadata", "some-value")
        );

        discovery.publish(record, ar -> {
            if (ar.succeeded()) {
                System.out.println("publish service succeed !");
            } else {
                System.out.println("publish service failed !");
            }
        });

        discovery.getRecord(
                new JsonObject().put("name", "some-data-source-service"),
                ar -> {
                    if (ar.succeeded() && ar.result() != null) {
                        ServiceReference reference = discovery.getReferenceWithConfiguration(
                                ar.result(),
                                new JsonObject()
                                        .put("url", "jdbc:mysql://192.168.2.242:3306/maanshan")
                                        .put("driver_class", "com.mysql.jdbc.Driver")
                                        .put("username", "root")
                                        .put("password", "minorfish")
                                        .put("max_pool_size", 10)
                        );

                        JDBCClient client = reference.getAs(JDBCClient.class);
                        System.out.println("jdbc client => " + client);

                        client.getConnection(conn -> {
                            if (conn.succeeded()) {
                                SQLConnection connection = conn.result();
                                connection.query("select * from hw_bag order by id desc limit 1", rs -> {
                                    if (rs.succeeded()) {
                                        System.out.println(rs.result());
                                    }
                                });
                            }
                        });
//                        reference.release();
                    } else {
                        System.out.println("get datasource record failed !");
                    }
                }
        );
    }

}
