package com.app.app.payload;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductRequest {
    Integer id;
    String name;
    BigDecimal price;
    BigDecimal discount;
    Integer cateId;
    List<String> images;

    public ProductRequest() {
        this(null, null, null, null, null, new ArrayList<>());
    }

    public ProductRequest(Integer id, String name, BigDecimal price, BigDecimal discount, Integer cateId, List<String> images) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.cateId = cateId;
        this.images = images;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
