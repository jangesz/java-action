package org.tic.vertx.examples.web.request.enums;

public interface JsonBodySchema {
    String ORDER_ARRAY_SCHEMA = "{\n" +
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
}
