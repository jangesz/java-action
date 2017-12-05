package org.tic.vertx.examples.web.ueditor.service.impl;

import com.github.witoldsz.ultm.TxManager;
import com.google.common.base.Preconditions;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.tic.vertx.examples.web.ueditor.dao.JooqTx;
import org.tic.vertx.examples.web.ueditor.service.UeditorService;

import javax.inject.Inject;

import static org.tic.vertx.examples.web.ueditor.jooq.Tables.T_UEDITOR;

public class UeditorServiceImpl implements UeditorService {
    private final TxManager txManager;
    private final DSLContext dsl;

    @Inject
    public UeditorServiceImpl(TxManager txManager, @JooqTx DSLContext dsl) {
        this.txManager = txManager;
        this.dsl = dsl;
    }

    @Override
    public void save(String html) {
        try {
            int ret = txManager.txResult(() -> {
                int flag = this.dsl.insertInto(T_UEDITOR,
                        T_UEDITOR.CONTENT, T_UEDITOR.CREATED_AT)
                        .values(new String(html.getBytes(), "UTF-8"), System.currentTimeMillis())
                        .execute();
                Preconditions.checkState(1 == flag, "新增失败");
                return flag;
            });
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String getPostById(Long id) {
        try {
            String html = txManager.txResult(() -> {
                Record record = this.dsl.resultQuery("select * from t_ueditor where id = " + id).fetchOne();
                return record.get(T_UEDITOR.CONTENT);
            });
            return html;
        } catch (Exception e) {
            return "no result";
        }
    }

    @Override
    public void update(String html) {
        try {
            int ret = txManager.txResult(() -> {
                int flag = this.dsl.update(T_UEDITOR).set(T_UEDITOR.CONTENT, html).where(T_UEDITOR.ID.eq(1L)).execute();
//                int flag = this.txJooq.insertInto(T_UEDITOR,
//                        T_UEDITOR.CONTENT, T_UEDITOR.CREATED_AT)
//                        .values(new String(html.getBytes(), "UTF-8"), System.currentTimeMillis())
//                        .execute();
                Preconditions.checkState(1 == flag, "更新失败");
                return flag;
            });
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
