package com.ducbao.common.model.enums;

public enum StatusPaymentEnum {
    PENDING("PENDING"), SUCCESS("SUCCESS"), FAILURE("FAILURE");
    ;
    private final String value;
    StatusPaymentEnum(String value) {
        this.value = value;
    }
}
