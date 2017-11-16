package org.tic.vertx.examples.web.request.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.ValidationMessage;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.api.validation.CustomValidator;
import io.vertx.ext.web.api.validation.HTTPRequestValidationHandler;
import io.vertx.ext.web.api.validation.ParameterType;
import io.vertx.ext.web.api.validation.ValidationException;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.ResponseContentTypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tic.vertx.examples.web.request.HttpEndpointVerticle;
import org.tic.vertx.examples.web.request.JsonBodyParser;
import org.tic.vertx.examples.web.request.VertxContentType;
import org.tic.vertx.examples.web.request.enums.JsonBodyValidations;

public class HttpValidationVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(HttpEndpointVerticle.class);

    @Override
    public void start() throws Exception {
        String orderAddJsonSchema = "[\n" +
                "    {\n" +
                "        \"order_no\": \"2017111401\",\n" +
                "        \"amount\": 988.00,\n" +
                "        \"consignee\": \"王先生\",\n" +
                "        \"phone\": \"13913580888\",\n" +
                "        \"region\": \"江苏省苏州市吴中区\",\n" +
                "        \"address\": \"XX路999号\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"order_no\": \"2017111402\",\n" +
                "        \"amount\": 388.00,\n" +
                "        \"consignee\": \"田先生\",\n" +
                "        \"phone\": \"13913580000\",\n" +
                "        \"region\": \"江苏省苏州市吴中区\",\n" +
                "        \"address\": \"林泉街333号\"\n" +
                "    }\n" +
                "]";

        String orderAddJsonSchema1 = "{\n" +
                "  \"$schema\": \"http://json-schema.org/draft-04/schema#\",\n" +
                "  \"title\": \"Order Array\",\n" +
                "  \"description\": \"A order array\",\n" +
                "  \"type\": \"array\",\n" +
                "  \"items\": {\n" +
                "    \"title\": \"Order\",\n" +
                "    \"type\": \"object\",\n" +
                "    \"properties\": {\n" +
                "      \"order_no\": {\n" +
                "        \"type\": \"string\",\n" +
                "        \"description\": \"\"\n" +
                "      },\n" +
                "      \"amount\": {\n" +
                "        \"description\": \"Name of the product\",\n" +
                "        \"type\": \"number\",\n" +
                "        \"minimun\": 0,\n" +
                "        \"exclusiveMinimum\": true\n" +
                "      },\n" +
                "      \"consignee\": {\n" +
                "        \"type\": \"string\",\n" +
                "        \"minLength\": 1\n" +
                "      },\n" +
                "      \"phone\": {\n" +
                "        \"type\": \"string\",\n" +
                "        \"minLength\": 11,\n" +
                "        \"maxLength\": 11\n" +
                "      },\n" +
                "      \"region\": {\n" +
                "        \"type\": \"string\",\n" +
                "        \"minLength\": 1\n" +
                "      },\n" +
                "      \"address\": {\n" +
                "        \"type\": \"string\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"required\": [\"order_no\", \"amount\", \"consignee\", \"phone\", \"region\"]\n" +
                "  }\n" +
                "}";
        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());
        router.route().handler(ResponseContentTypeHandler.create());

        // Create Validation Handler with some stuff
//        HTTPRequestValidationHandler validationHandler =
//                HTTPRequestValidationHandler.create()
//                        .addQueryParam("parameterName", ParameterType.INT, true)
//                        .addFormParamWithPattern("formParameterName", "a{4}", true)
//                        .addPathParam("pathParam", ParameterType.FLOAT);

        HTTPRequestValidationHandler validationHandler1 = HTTPRequestValidationHandler.create();
//        validationHandler1.addJsonBodySchema(orderAddJsonSchema1);
        validationHandler1.addCustomValidatorFunction(JsonBodyValidations.ORDER_ARRAY.validator());



        router.post("/order")
                .consumes(VertxContentType.APPLICATION_JSON)
                .produces(VertxContentType.APPLICATION_JSON_UTF8)
                .handler(validationHandler1)
                //Mount your failure handler
                .failureHandler((routingContext) -> {
                    Throwable failure = routingContext.failure();
                    System.out.println(failure.getMessage());
                    if (failure instanceof ValidationException) {
                        //JSON_INVALID
                        System.out.println(((ValidationException) failure).type());
                        // Something went wrong during validation!
                        String validationErrorMessage = failure.getMessage();
                        logger.error(validationErrorMessage);
                        routingContext.response().setStatusCode(400).end(failure.getMessage());
                    } else {
                        routingContext.response().end(failure.getMessage());
                    }
                })
                .handler(this::addOrder);

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

    private void addOrder(RoutingContext context) {
        if (JsonBodyParser.parse(context.getBodyAsString(), JsonObject.class)) {
            logger.info("getBodyAsJson: {}", context.getBodyAsJson());
        } else if (JsonBodyParser.parse(context.getBodyAsString(), JsonArray.class)) {
            logger.info("getBodyAsJsonArray: {}", context.getBodyAsJsonArray());
        } else {
            logger.warn("parse content body failed !");
        }
        context.response().end("addOrder");
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(HttpValidationVerticle.class.getName());
    }

}
