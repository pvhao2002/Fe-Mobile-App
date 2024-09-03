package com.app.app.model;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class User {
    int userId;
    String username;
    String password;
    String fullName;
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public User() {
      this(0, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);
    }

    public User(JSONObject jsonObject) throws JSONException {
        this(
                jsonObject.getInt("userId"),
                jsonObject.getString("username"),
                jsonObject.getString("password"),
                jsonObject.getString("fullName")
        );
    }

    public User(int userId, String username, String password, String fullName) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }
}
