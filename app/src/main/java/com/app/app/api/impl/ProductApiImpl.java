package com.app.app.api.impl;

import com.app.app.api.BaseAPI;
import com.app.app.api.CategoryAPI;
import com.app.app.api.ProductAPI;

public class ProductApiImpl extends BaseAPI {
    private static ProductAPI productAPI;

    public static ProductAPI getInstance() {
        if (productAPI == null)
            productAPI = createService(ProductAPI.class, BASE_HREF.concat("product/"));
        return productAPI;
    }
}
