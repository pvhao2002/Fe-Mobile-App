package com.app.app.callback;

public interface OrderCallback {
    void onUpdateStatusOrder(Integer orderId, String status);
}
