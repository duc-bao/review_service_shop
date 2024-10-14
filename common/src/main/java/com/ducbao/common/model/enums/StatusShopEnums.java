package com.ducbao.common.model.enums;

import lombok.Data;


public enum StatusShopEnums {
        ACTIVE("ACTIVE"), DEACTIVE("DEACTIVE");
    private final String value;
    private StatusShopEnums(String value) {
        this.value = value;
    }
}
