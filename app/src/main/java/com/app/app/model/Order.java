package com.app.app.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class Order {
    Integer orderId;
    String orderDate;
    String orderStatus;
    BigDecimal total;

    ArrayList<OrderItem> orderItems;

    public Order(Integer orderId, String orderDate, String orderStatus, BigDecimal total, ArrayList<OrderItem> orderItems) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.total = total;
        this.orderItems = orderItems;
    }

    public Order(JSONObject json) throws JSONException {
        this(
                json.optInt("orderId"),
                json.getString("orderDate"),
                json.optString("status"),
                new BigDecimal(json.optDouble("totalNetPrice")),
                new ArrayList<>()
        );
        JSONArray orderItemsJson = json.optJSONArray("orderItems");
        for (int i = 0; i < orderItemsJson.length(); i++) {
            if (orderItemsJson.optJSONObject(i) != null) {
                this.orderItems.add(new OrderItem(orderItemsJson.optJSONObject(i)));
            }
        }
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayList<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
