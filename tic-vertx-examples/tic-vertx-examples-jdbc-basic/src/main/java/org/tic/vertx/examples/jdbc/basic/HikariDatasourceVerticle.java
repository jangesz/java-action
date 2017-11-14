package org.tic.vertx.examples.jdbc.basic;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.jdbc.spi.impl.HikariCPDataSourceProvider;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public class HikariDatasourceVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(HikariDatasourceVerticle.class);

    @Override
    public void start() throws Exception {
        JsonObject hikariConfig = new JsonObject();
        hikariConfig
//                .put("dataSourceClassName", '"')
                .put("driverClassName", "com.mysql.jdbc.Driver")
                .put("jdbcUrl", "jdbc:mysql://192.168.2.242:3306/crux?useUnicode=true&amp;characterEncoding=UTF-8")
                .put("username", "root")
                .put("password", "minorfish")
                .put("autoCommit", false)
                .put("connectionTimeut", 30000)
                .put("idleTimeout", 600000)
                .put("maxLifetime", 1800000)
                .put("maximumPoolSize", 10)
                ;
        HikariCPDataSourceProvider provider = new HikariCPDataSourceProvider();
        DataSource dataSource = provider.getDataSource(hikariConfig);

        JDBCClient client = JDBCClient.create(vertx, dataSource);

        client.getConnection(ar -> {
            if (ar.succeeded()) {
                SQLConnection conn = ar.result();
                conn.query("select * from todo", res -> {
                    if (res.succeeded()) {
                        ResultSet rs = res.result();
                        System.out.println(rs.toJson());
                    } else {
                        logger.warn("can't fetch results from table todo !");
                    }
                });
                conn.close();
            } else {
                logger.error("can't get connection !");
            }
        });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(HikariDatasourceVerticle.class.getName());
    }
}
