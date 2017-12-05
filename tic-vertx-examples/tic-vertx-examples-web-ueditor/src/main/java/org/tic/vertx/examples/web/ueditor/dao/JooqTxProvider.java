package org.tic.vertx.examples.web.ueditor.dao;

import com.github.witoldsz.ultm.ULTM;
import com.google.inject.Provider;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

import javax.inject.Inject;

public class JooqTxProvider implements Provider<DSLContext> {
    private final ULTM ultm;

    @Inject
    public JooqTxProvider(ULTM ultm) {
        this.ultm = ultm;
    }

    @Override
    public DSLContext get() {
        Configuration configuration = new DefaultConfiguration()
                .set(ultm.getManagedDataSource())
                .set(SQLDialect.MYSQL);
        return DSL.using(configuration);
    }
}
