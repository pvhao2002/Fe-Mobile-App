package com.app.app.api.impl;

import com.app.app.api.BaseAPI;
import com.app.app.api.UserAPI;

public class UserApiImpl extends BaseAPI {
    private static UserAPI userAPI;

    public static UserAPI getInstance() {
        if (userAPI == null) userAPI = createService(UserAPI.class, BASE_HREF.concat("auth/"));
        return userAPI;
    }
}
