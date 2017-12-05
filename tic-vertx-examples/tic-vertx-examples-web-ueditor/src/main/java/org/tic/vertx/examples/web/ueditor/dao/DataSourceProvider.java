package org.tic.vertx.examples.web.ueditor.dao;

import com.google.inject.Provider;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.spi.impl.HikariCPDataSourceProvider;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DataSourceProvider implements Provider<DataSource> {
    @Override
    public DataSource get() {
        try {
            JsonObject hikariConfig = new JsonObject();
            hikariConfig
                    .put("driverClassName", "com.mysql.jdbc.Driver")
                    //TODO，这里&characterEncoding=UTF-8，中的&不能省略，否则插入后乱码，待验证
                    .put("jdbcUrl", "jdbc:mysql://192.168.2.242:3306/test2?useUnicode=true&characterEncoding=UTF-8")
                    .put("username", "root")
                    .put("password", "minorfish")
                    .put("autoCommit", false)
                    .put("connectionTimeut", 30000)
                    .put("idleTimeout", 600000)
                    .put("maxLifetime", 1800000)
                    .put("maximumPoolSize", 2)
            ;
            HikariCPDataSourceProvider provider = new HikariCPDataSourceProvider();
            return provider.getDataSource(hikariConfig);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
