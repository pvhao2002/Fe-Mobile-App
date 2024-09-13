package com.app.app.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Cart {
    Integer cartId;
    Integer quantity;
    BigDecimal totalPrice;
    BigDecimal totalNetPrice;
    BigDecimal discount;
    BigDecimal total;
    ArrayList<CartItem> cartItems;

    public Cart(JSONObject json) {
        this(
                json.optInt("cartId", 0),
                json.optInt("quantity", 0),
                new BigDecimal(json.optDouble("totalPrice", 0)),
                new BigDecimal(json.optDouble("totalNetPrice", 0)),
                new BigDecimal(json.optDouble("totalDiscount", 0)),
                new BigDecimal(json.optDouble("total", 0)),
                new ArrayList<>()
        );
        JSONArray cartItems = json.optJSONArray("cartItems");
        if (cartItems != null) {
            for (int i = 0; i < cartItems.length(); i++) {
                this.cartItems.add(new CartItem(cartItems.optJSONObject(i)));
            }
        }
    }

    public Cart() {
        this(0, 0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, new ArrayList<>());
    }

    public Cart(Integer cartId, Integer quantity, BigDecimal totalPrice, BigDecimal totalNetPrice, BigDecimal discount, BigDecimal total, ArrayList<CartItem> cartItems) {
        this.cartId = cartId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.totalNetPrice = totalNetPrice;
        this.total = total;
        this.cartItems = cartItems;
        this.discount = discount;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalNetPrice() {
        return totalNetPrice;
    }

    public void setTotalNetPrice(BigDecimal totalNetPrice) {
        this.totalNetPrice = totalNetPrice;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public ArrayList<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(ArrayList<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", totalNetPrice=" + totalNetPrice +
                ", discount=" + discount +
                ", total=" + total +
                ", cartItems=" + cartItems +
                '}';
    }
}
