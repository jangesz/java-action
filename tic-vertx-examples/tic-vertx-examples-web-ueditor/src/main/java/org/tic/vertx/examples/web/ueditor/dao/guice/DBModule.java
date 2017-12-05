package org.tic.vertx.examples.web.ueditor.dao.guice;

import com.github.witoldsz.ultm.TxManager;
import com.github.witoldsz.ultm.ULTM;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.jooq.DSLContext;
import org.tic.vertx.examples.web.ueditor.dao.*;

import javax.sql.DataSource;

// 定义一个Module管理接口与实现类的绑定关系
public class DBModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DataSource.class).toProvider(DataSourceProvider.class).in(Singleton.class);

        bind(DSLContext.class).toProvider(JooqProvider.class).in(Singleton.class);
        bind(DSLContext.class).annotatedWith(JooqTx.class).toProvider(JooqTxProvider.class).in(Singleton.class);

        bind(ULTM.class).toProvider(ULTMProvider.class).in(Singleton.class);
        bind(TxManager.class).toProvider(TxManagerProvider.class).in(Singleton.class);
    }

}
