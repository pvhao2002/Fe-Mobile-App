package com.app.app.model;

import java.io.Serializable;

public class ProductImage implements Serializable {
    private Integer productImageId;
    private String url;

    public ProductImage() {
        this(null, null);
    }

    public ProductImage(Integer productImageId, String url) {
        this.productImageId = productImageId;
        this.url = url;
    }

    public Integer getProductImageId() {
        return productImageId;
    }

    public void setProductImageId(Integer productImageId) {
        this.productImageId = productImageId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
