package org.tic.vertx.examples.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebExample05FileUploadVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(WebExample05FileUploadVerticle.class);

    @Override
    public void start() throws Exception {

        Router router = Router.router(vertx);

        router.route("/api/file/upload").handler(ctx -> {
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
            .listen(9999, rc -> {
                if (rc.succeeded()) {
                    logger.info("file upload verticle started succeed !");
                } else {
                    logger.error("file upload verticle started failed !" + rc.cause().getMessage());
                }
            });


    }

    private void fileUpload(RoutingContext context) {
        if ( null == context.fileUploads() || context.fileUploads().isEmpty() ) {
            context.response().end("请上传文件");
            return;
        }

        FileUpload file = (FileUpload) context.fileUploads().toArray()[0];
//        file.

    }

}
