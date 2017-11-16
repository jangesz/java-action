package org.tic.vertx.examples.web.request;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class JsonBodyParser {

    public static final String JSON_OBJECT = "json_object";
    public static final String JSON_ARRAY = "json_array";

//    public static <T> T parseAs(String json, T clazz) {
//        try {
//            if (clazz.getName().equals(JsonObject.class.getName())) {
//
//            }
////            if (clazz. instanceof JsonObject) {
////                return (T) (new JsonObject(json));
////            } else if (clazz instanceof JsonArray) {
////                return (T) (new JsonArray(json));
////            }
//        } catch (Exception e) {
//            throw new RuntimeException("请求参数解析错误，请检查参数是否符合格式: {" + clazz.getClass().getName() + "}");
//        }
//        throw new RuntimeException("请求参数没有找到解析类型");
//    }

    public static boolean parse(String json, Class clazz) {
        try {
            if (clazz.getName().equals(JsonObject.class.getName())) {
                new JsonObject(json);
                return true;
            } else if (clazz.getName().equals(JsonArray.class.getName())) {
                new JsonArray(json);
                return true;
            }
        } catch (Exception e) {
            return false;
//            throw new RuntimeException("请求参数解析错误，请检查参数是否符合格式: {" + clazz.getClass().getName() + "}");
        }
        return false;
//        throw new RuntimeException("请求参数没有找到解析类型");
    }

}
