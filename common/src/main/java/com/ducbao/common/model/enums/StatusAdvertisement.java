package com.ducbao.common.model.enums;

public enum StatusAdvertisement {
    OPEN("OPEN"), CLOSE("CLOSE"),
    ;
    private String value;
    private StatusAdvertisement(String value) {
        this.value = value;
    }
}
