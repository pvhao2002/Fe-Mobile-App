package com.app.app.model;

import org.json.JSONObject;

import java.math.BigDecimal;

public class CartItem {
    Integer cartItemId;
    Integer quantity;
    BigDecimal price;
    BigDecimal netPrice;
    BigDecimal total;
    Product product;

    public CartItem(JSONObject json) {
        this(
                json.optInt("cartItemId", 0),
                json.optInt("quantity", 0),
                new BigDecimal(json.optDouble("price", 0)),
                new BigDecimal(json.optDouble("netPrice", 0)),
                new BigDecimal(json.optDouble("total", 0)),
                new Product()
        );
        JSONObject p = json.optJSONObject("product");
        if (p != null) {
            this.product = new Product(p);
        }
    }

    public CartItem(Integer cartItemId, Integer quantity, BigDecimal price, BigDecimal netPrice, BigDecimal total, Product product) {
        this.cartItemId = cartItemId;
        this.quantity = quantity;
        this.price = price;
        this.netPrice = netPrice;
        this.total = total;
        this.product = product;
    }

    public CartItem(Integer cartItemId, Integer quantity, BigDecimal price, BigDecimal netPrice, BigDecimal total) {
        this(cartItemId, quantity, price, netPrice, total, null);
    }

    public CartItem() {
        this(0, 0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    public Integer getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Integer cartItemId) {
        this.cartItemId = cartItemId;
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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "cartItemId=" + cartItemId +
                ", quantity=" + quantity +
                ", price=" + price +
                ", netPrice=" + netPrice +
                ", total=" + total +
                ", product=" + product +
                '}';
    }
}
