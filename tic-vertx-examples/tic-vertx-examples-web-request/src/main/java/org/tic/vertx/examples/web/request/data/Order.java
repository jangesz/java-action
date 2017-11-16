package org.tic.vertx.examples.web.request.data;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject(generateConverter = true)
public class Order {
    private String orderNo;
    private Double amount;
    private String consignee;
    private String region;
    private String address;

    public Order() {}

    public Order(Order other) {
        this.orderNo = other.orderNo;
        this.amount = other.amount;
        this.consignee = other.consignee;
        this.region = other.region;
        this.address = other.address;
    }

    public Order(JsonObject obj) {
        OrderConverter.fromJson(obj, this);
    }

    public Order(String json) {
        OrderConverter.fromJson(new JsonObject(json), this);
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        OrderConverter.toJson(this, json);
        return json;
    }
}
