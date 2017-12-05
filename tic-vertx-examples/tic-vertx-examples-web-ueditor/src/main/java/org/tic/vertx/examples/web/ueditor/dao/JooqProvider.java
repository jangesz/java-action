package org.tic.vertx.examples.web.ueditor.dao;

import com.google.inject.Provider;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

import javax.inject.Inject;
import javax.sql.DataSource;

public class JooqProvider implements Provider<DSLContext> {

    private final DataSource dataSource;

    @Inject
    public JooqProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DSLContext get() {
        Configuration configuration = new DefaultConfiguration()
                .set(dataSource)
                .set(SQLDialect.MYSQL);
        return DSL.using(configuration);
    }

}
