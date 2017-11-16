/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.tic.vertx.examples.web.request.data;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link org.tic.vertx.examples.web.request.data.Order}.
 *
 * NOTE: This class has been automatically generated from the {@link org.tic.vertx.examples.web.request.data.Order} original class using Vert.x codegen.
 */
public class OrderConverter {

  public static void fromJson(JsonObject json, Order obj) {
    if (json.getValue("address") instanceof String) {
      obj.setAddress((String)json.getValue("address"));
    }
    if (json.getValue("amount") instanceof Number) {
      obj.setAmount(((Number)json.getValue("amount")).doubleValue());
    }
    if (json.getValue("consignee") instanceof String) {
      obj.setConsignee((String)json.getValue("consignee"));
    }
    if (json.getValue("orderNo") instanceof String) {
      obj.setOrderNo((String)json.getValue("orderNo"));
    }
    if (json.getValue("region") instanceof String) {
      obj.setRegion((String)json.getValue("region"));
    }
  }

  public static void toJson(Order obj, JsonObject json) {
    if (obj.getAddress() != null) {
      json.put("address", obj.getAddress());
    }
    if (obj.getAmount() != null) {
      json.put("amount", obj.getAmount());
    }
    if (obj.getConsignee() != null) {
      json.put("consignee", obj.getConsignee());
    }
    if (obj.getOrderNo() != null) {
      json.put("orderNo", obj.getOrderNo());
    }
    if (obj.getRegion() != null) {
      json.put("region", obj.getRegion());
    }
  }
}