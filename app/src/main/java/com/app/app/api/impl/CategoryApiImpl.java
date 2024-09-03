package com.app.app.api.impl;

import com.app.app.api.BaseAPI;
import com.app.app.api.CategoryAPI;

public class CategoryApiImpl extends BaseAPI {
    private static CategoryAPI categoryAPI;

    public static CategoryAPI getInstance() {
        if (categoryAPI == null)
            categoryAPI = createService(CategoryAPI.class, BASE_HREF.concat("category/"));
        return categoryAPI;
    }
}