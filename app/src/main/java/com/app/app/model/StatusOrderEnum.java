package com.app.app.model;

public enum StatusOrderEnum {
    PENDING("PENDING") {
        @Override
        public String toString() {
            return "Đang chở xác nhận";
        }
    },
    ACCEPTED("ACCEPTED") {
        @Override
        public String toString() {
            return "Đang chờ khách hàng đến lấy";
        }
    },
    DONE("DONE") {
        @Override
        public String toString() {
            return "Đã nhận";
        }
    },
    CANCELLED("CANCELLED") {
        @Override
        public String toString() {
            return "Đã hủy";
        }
    };

    private final String status;

    StatusOrderEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
