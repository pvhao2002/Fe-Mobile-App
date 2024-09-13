package com.app.app.model;

import org.json.JSONObject;

import java.math.BigDecimal;

public class OrderItem {
    Integer orderItemId;
    Integer quantity;
    BigDecimal price;
    BigDecimal netPrice;
    Product product;

    public OrderItem(Integer orderItemId, Integer quantity, BigDecimal price, BigDecimal netPrice, Product product) {
        this.orderItemId = orderItemId;
        this.quantity = quantity;
        this.price = price;
        this.netPrice = netPrice;
        this.product = product;
    }

    public OrderItem(JSONObject json) {
        this(
                json.optInt("orderItemId"),
                json.optInt("quantity"),
                new BigDecimal(json.optDouble("price")),
                new BigDecimal(json.optDouble("netPrice")),
                new Product()
        );
        JSONObject productJson = json.optJSONObject("product");
        if (productJson != null) {
            this.product = new Product(productJson);
        }
    }

    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(BigDecimal netPrice) {
        this.netPrice = netPrice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
