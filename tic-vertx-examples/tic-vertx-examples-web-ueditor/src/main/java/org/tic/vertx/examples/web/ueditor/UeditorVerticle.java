package org.tic.vertx.examples.web.ueditor;

import com.google.inject.Guice;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.templ.FreeMarkerTemplateEngine;
import org.jooq.util.derby.sys.Sys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tic.vertx.examples.web.ueditor.dao.guice.DBModule;
import org.tic.vertx.examples.web.ueditor.modules.UeditorModule;
import org.tic.vertx.examples.web.ueditor.service.UeditorService;

import javax.inject.Inject;

public class UeditorVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(UeditorVerticle.class);

    private final FreeMarkerTemplateEngine templateEngine = FreeMarkerTemplateEngine.create();

    @Inject
    private UeditorService ueditorService;

    @Override
    public void start() throws Exception {
        Guice.createInjector(new DBModule(), new UeditorModule()).injectMembers(this);

        Router router = Router.router(vertx);

        router.route("/static/*").handler(StaticHandler.create());
        router.route().handler(BodyHandler.create());
        router.route().produces("application/json;charset=utf-8");

        router.get("/").handler(this::index);
        router.post("/save").handler(this::editor);
        router.post("/update").handler(this::update);
        router.get("/post/:id").handler(this::postGet);

        // 上传文件
        router.route("/upload").handler(ctx -> {
            // 如果您需要在一个阻塞处理器中处理一个multipart类型的表单数据
            // 您需要首先使用一个非阻塞的处理器来设置
            ctx.request().setExpectMultipart(true);
            ctx.next();
        }).blockingHandler(ctx -> {
            // 执行某些阻塞操作
            fileUpload(ctx);
        });

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(9999);
    }

    private void update(RoutingContext context) {
        String html = context.request().getFormAttribute("content");
        this.ueditorService.update(html);
        context.response().end(context.request().getFormAttribute("content"));
    }

    private void postGet(RoutingContext context) {
        String id = context.pathParam("id");
        String html = this.ueditorService.getPostById(Long.valueOf(id));
        context.response().putHeader("content-type", "text/html;charset=utf-8").end(html);
    }

    private void editor(RoutingContext context) {
        String html = context.request().getFormAttribute("content");
        this.ueditorService.save(html);
        context.response().end(context.request().getFormAttribute("content"));
    }

    private void fileUpload(RoutingContext context) {
        if (null == context.fileUploads() || context.fileUploads().isEmpty()) {
            context.response().end("请上传文件");
            return;
        }

        FileUpload file = (FileUpload) context.fileUploads().toArray()[0];

    }

    private void index(RoutingContext context) {
        context.response().end("helloworld");
//        context.put("title", "Wiki home");
//        templateEngine.render(context, "templates", "/index.ftl", ar -> {
//            if (ar.succeeded()) {
//                context.response().putHeader("Content-Type", "text/html");
//                context.response().end(ar.result());
//            } else {
//                context.fail(ar.cause());
//            }
//        });
    }
}
