package org.tic.vertx.examples.jdbc.basic.spi;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.spi.DataSourceProvider;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DruidDatasourceProvider implements DataSourceProvider {
    @Override
    public int maximumPoolSize(DataSource dataSource, JsonObject config) throws SQLException {
        return 10;
    }

    @Override
    public DataSource getDataSource(JsonObject config) throws SQLException {
        return null;
    }

    @Override
    public void close(DataSource dataSource) throws SQLException {

    }
}
