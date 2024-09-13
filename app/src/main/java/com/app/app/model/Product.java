package com.app.app.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Product implements Serializable {
    int productId;
    String name;
    BigDecimal price;
    BigDecimal netPrice;
    BigDecimal discount;
    String thumbnail;
    List<ProductImage> images = new ArrayList<>();

    public Product() {
        this(0, "", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, "", new ArrayList<>());
    }

    public Product(JSONObject json) {
        this(
                json,
                new ArrayList<>()
        );
        JSONArray imagesArr = json.optJSONArray("images");
        if (imagesArr != null) {
            for (int i = 0; i < imagesArr.length(); i++) {
                this.images.add(new ProductImage(imagesArr.optJSONObject(i)));
            }
        }
    }

    public Product(JSONObject json, List<ProductImage> images) {
        this(
                json.optInt("productId", 0),
                json.optString("name", ""),
                new BigDecimal(json.optDouble("price", Double.valueOf(0))),
                new BigDecimal(json.optDouble("netPrice", Double.valueOf(0))),
                new BigDecimal(json.optDouble("discount", Double.valueOf(0))),
                json.optString("thumbnail", ""),
                images
        );
    }

    public Product(int productId, String name, BigDecimal price, BigDecimal netPrice, BigDecimal discount, String thumbnail, List<ProductImage> images) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.netPrice = netPrice;
        this.discount = discount;
        this.thumbnail = thumbnail;
        this.images = images;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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

    public BigDecimal getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(BigDecimal netPrice) {
        this.netPrice = netPrice;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", netPrice=" + netPrice +
                ", discount=" + discount +
                ", thumbnail='" + thumbnail + '\'' +
                ", images=" + images +
                '}';
    }
}
