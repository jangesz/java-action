package org.tic.vertx.examples.jdbc.basic;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(BasicVerticle.class);

    @Override
    public void start() throws Exception {
        JsonObject config = new JsonObject();
        config.put("url", "jdbc:mysql://192.168.2.242:3306/crux");
        config.put("driver_class", "com.mysql.jdbc.Driver");
        config.put("user", "root");
        config.put("password", "minorfish");
        JDBCClient client = JDBCClient.createShared(vertx, config);

        client.getConnection(conn -> {
            if (conn.succeeded()) {
                SQLConnection connection = conn.result();
                connection.query("select * from todo", rs -> {
                    if (rs.succeeded()) {
                        ResultSet resultSet = rs.result();
                        System.out.println(resultSet.toJson());
                    } else {
                        logger.warn("query result set failed !");
                    }
                });
            } else {
                logger.error("can't get connection !");
            }
        });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(BasicVerticle.class.getName());
    }

}
