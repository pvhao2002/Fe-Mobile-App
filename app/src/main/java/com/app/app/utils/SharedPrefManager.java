package com.app.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.app.model.User;

import org.apache.commons.lang3.StringUtils;

public final class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "login";
    private static final String KEY_ID = "key-id";
    private static final String KEY_NAME = "key-name";
    private static final String KEY_EMAIL = "key-email";
    private static final String KEY_PASSWORD = "key-password";
    private static SharedPrefManager mInstance;
    private static Context ctx;

    private SharedPrefManager(Context context) {
        ctx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void userLogin(User user) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID, user.getUserId() + "");
        editor.putString(KEY_NAME, user.getFullName());
        editor.putString(KEY_EMAIL, user.getUsername());
        editor.putString(KEY_PASSWORD, user.getPassword());
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL, null) != null;
    }

    public User getUser() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        User u = new User();
        u.setUserId(sharedPreferences.getInt(KEY_ID, 0));
        u.setFullName(sharedPreferences.getString(KEY_NAME, StringUtils.EMPTY));
        u.setUsername(sharedPreferences.getString(KEY_EMAIL, StringUtils.EMPTY));
        u.setPassword(sharedPreferences.getString(KEY_PASSWORD, StringUtils.EMPTY));
        return u;
    }

    public void logout() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
