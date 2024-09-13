package com.app.app.api.impl;

import com.app.app.api.BaseAPI;
import com.app.app.api.OrderAPI;

public class OrderApiImpl extends BaseAPI {
    private static OrderAPI orderApi;

    public static OrderAPI getInstance() {
        if (orderApi == null)
            orderApi = createService(OrderAPI.class, BASE_HREF.concat("order/"));
        return orderApi;
    }
}
