package com.app.app.model;

import org.json.JSONObject;

import java.io.Serializable;

public class Category implements Serializable {
    int categoryId;
    String name;
    String status;

    public Category() {
        this(0, "", "");
    }
    public Category(JSONObject json) {
        this(
                json.optInt("categoryId", 0),
                json.optString("name", ""),
                json.optString("status", "")
        );
    }

    public Category(int categoryId, String name, String status) {
        this.categoryId = categoryId;
        this.name = name;
        this.status = status;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
