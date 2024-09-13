package com.app.app.api.impl;

import com.app.app.api.BaseAPI;
import com.app.app.api.CartAPI;

public class CartApiImpl extends BaseAPI {
    private static CartAPI cartApi;

    public static CartAPI getInstance() {
        if (cartApi == null)
            cartApi = createService(CartAPI.class, BASE_HREF.concat("cart/"));
        return cartApi;
    }
}
