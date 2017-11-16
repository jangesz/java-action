package org.tic.vertx.examples.web.request;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.ResponseContentTypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * 如果知道参数是String类型，可以使用getBodyAsString()
 * 如果知道参数是Json类型，可以使用getBodyAsJson()
 * 如果知道参数是JsonArray类型，可以使用getBodyAsJsonArray()
 *
 *
 *
 *
 *
 */
public class HttpEndpointVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(HttpEndpointVerticle.class);

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        //表单提交
        router.post("/account").handler(this::addAccount);
        //body提交
        router.post("/user").handler(this::addUser);

        // Content-Type处理器，ResponseContentTypeHandler会自动设置Content-Type消息头，而不需要
        // 自己手动去rc.response.put("Content-Type", "application/json;charset=utf-8")，只需要
        // 在route上produces("application/json")即可
        router.route().handler(ResponseContentTypeHandler.create());
        router.post("/order")
                .consumes(VertxContentType.APPLICATION_JSON)
                .produces(VertxContentType.APPLICATION_JSON_UTF8)
                .handler(this::addOrder);


        router.get("/order")
                .produces(VertxContentType.APPLICATION_JSON_UTF8)
                .produces(VertxContentType.TEXT_XML_UTF8)
                .handler(this::getOrder);

        vertx.createHttpServer()
            .requestHandler(router::accept)
            .listen(9999, res -> {
                if (res.succeeded()) {
                    logger.info("http endpoint server started in port 9999 !");
                } else {
                    logger.error("http endpoint server started failed !");
                }
            });
    }

    private void getOrder(RoutingContext rc) {
        String type1 = rc.request().getParam("type");
        String type = rc.queryParam("type").get(0);
        System.out.println(type);
        System.out.println(type1);
        rc.setAcceptableContentType(VertxContentType.TEXT_XML_UTF8);
        if (rc.getAcceptableContentType().equals(VertxContentType.TEXT_XML_UTF8)) {
            rc.response().end("xml result");
        } else {
            rc.response().end("json result");
        }
    }

    private void addOrder(RoutingContext context) {
        printContext(context);
        context.response().end();
    }

    private void addUser(RoutingContext context) {
        printContext(context);
        context.response().end();
    }

    private void addAccount(RoutingContext context) {
        printContext(context);
        context.response().end();
    }

    private void printContext(RoutingContext context) {
        System.out.println(context.request().formAttributes());


        logger.info("get(key): {}", (String)context.get("username"));
        logger.info("pathParams: {}", context.pathParams());
        logger.info("queryParams: {}", context.queryParams());
        logger.info("acceptableLanguages: {}", context.acceptableLanguages());
        logger.info("cookieCount: {}", context.cookieCount());
        logger.info("cookies: {}", context.cookies());
        logger.info("currentRote: {}", context.currentRoute());
        logger.info("data: {}", context.data());
        logger.info("getAcceptableContentType: {}", context.getAcceptableContentType());
        logger.info("normalisedPath: {}", context.normalisedPath());
        logger.info("session: {}", context.session());
        logger.info("user: {}", context.user());

        logger.info("getBodyAsString: {}", context.getBodyAsString());
        if (JsonBodyParser.parse(context.getBodyAsString(), JsonObject.class)) {
            logger.info("getBodyAsJson: {}", context.getBodyAsJson());
        } else if (JsonBodyParser.parse(context.getBodyAsString(), JsonArray.class)) {
            logger.info("getBodyAsJsonArray: {}", context.getBodyAsJsonArray());
        } else {
            logger.warn("parse content body failed !");
        }
        context.addBodyEndHandler(h -> logger.info("addBodyEndHandler"));
        context.addHeadersEndHandler(h -> logger.info("addHeadersEndHandler"));
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(HttpEndpointVerticle.class.getName());
    }

}
