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

public class JDBCDatasourceHikariMySQLServiceDiscoveryVerticle3 extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(JDBCDatasourceHikariMySQLServiceDiscoveryVerticle3.class);

    @Override
    public void start() throws Exception {
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

        // 如果自己选定了某个数据库连接池，比如这里的HikariCP，那么配置信息一定要遵守HikariCP的配置
        // 而不能还是使用Vert.x JDBC提供的配置属性，如下是错误的（因为在HikariCP中的配置信息不是这样的）
        // .put("driver_class", "com.mysql.jdbc.Driver")
        // .put("user", "root")
        // .put("password", "minorfish")
        // .put("max_pool_size", 10);

        Record record = JDBCDataSource.createRecord("hikari-mysql-datasource",
                new JsonObject().put("url", "jdbc:mysql://192.168.2.242:3306/crux"),
                new JsonObject()
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
                );

        discovery.publish(record, ar -> {
            if (ar.succeeded()) {
                logger.info("publish jdbc datasource service succeed !");
            } else {
                logger.error("publish jdbc datasource service failed !");
            }
        });

        vertx.setTimer(3000, h -> {
            discovery.getRecord(new JsonObject().put("name", "hikari-mysql-datasource"), ar -> {
                if (ar.succeeded()) {
                    ServiceReference reference = discovery.getReference(ar.result());
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

                                connection.close();
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
        vertx.deployVerticle(JDBCDatasourceHikariMySQLServiceDiscoveryVerticle3.class.getName());
    }
}
