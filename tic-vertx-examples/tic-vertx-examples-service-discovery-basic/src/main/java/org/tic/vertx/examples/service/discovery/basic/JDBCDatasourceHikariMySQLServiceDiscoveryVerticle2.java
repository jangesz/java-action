package org.tic.vertx.examples.service.discovery.basic;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.JDBCDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDBCDatasourceHikariMySQLServiceDiscoveryVerticle2 extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(JDBCDatasourceHikariMySQLServiceDiscoveryVerticle2.class);

    @Override
    public void start() throws Exception {
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

        // 如果通过JDBCDatasource.getJDBCClient的方式来获取服务资源
        // 那么必须在createRecord的时候就设置好配置信息，可以设置在location中，也可以设置在metadata中
        // Vert.x会根据这两个地方的配置信息去设置JDBCClient
        Record record = JDBCDataSource.createRecord("hikari-mysql-datasource",
                new JsonObject()
                        .put("url", "jdbc:mysql://182.61.39.59:3306/crux")
                        // 这里通过provider_class来指定使用哪个数据库连接池
                        .put("provider_class", "io.vertx.ext.jdbc.spi.impl.HikariCPDataSourceProvider")
                        .put("driverClassName", "com.mysql.jdbc.Driver")
                        .put("jdbcUrl", "jdbc:mysql://192.168.2.242:3306/crux?useUnicode=true&amp;characterEncoding=UTF-8")
                        .put("username", "root")
                        .put("password", "minorfish")
                        .put("autoCommit", false)
                        .put("connectionTimeut", 30000)
                        .put("idleTimeout", 600000)
                        .put("maxLifetime", 1800000)
                        .put("maximumPoolSize", 10)
                ,
                new JsonObject()
                        .put("database", "some-raw-data")
                        // 把上面的注释掉，打开下面的注释也是可以的
                        // 这里通过provider_class来指定使用哪个数据库连接池
//                        .put("provider_class", "io.vertx.ext.jdbc.spi.impl.HikariCPDataSourceProvider")
//                        .put("driverClassName", "com.mysql.jdbc.Driver")
//                        .put("jdbcUrl", "jdbc:mysql://192.168.2.242:3306/crux?useUnicode=true&amp;characterEncoding=UTF-8")
//                        .put("username", "root")
//                        .put("password", "minorfish")
//                        .put("autoCommit", false)
//                        .put("connectionTimeut", 30000)
//                        .put("idleTimeout", 600000)
//                        .put("maxLifetime", 1800000)
//                        .put("maximumPoolSize", 10)
                );

        discovery.publish(record, ar -> {
            if (ar.succeeded()) {
                logger.info("publish jdbc datasource service succeed !");
            } else {
                logger.error("publish jdbc datasource service failed !");
            }
        });

        vertx.setTimer(3000, h -> {
            JDBCDataSource.getJDBCClient(discovery, new JsonObject().put("name", "hikari-mysql-datasource"), ar -> {
                if (ar.succeeded()) {
                    JDBCClient client = ar.result();
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
                } else {
                    logger.error("can't get jdbc client !");
                }
            });
        });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(JDBCDatasourceHikariMySQLServiceDiscoveryVerticle2.class.getName());
    }

}
