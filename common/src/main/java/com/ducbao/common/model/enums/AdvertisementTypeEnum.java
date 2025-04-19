package com.ducbao.common.model.enums;

public enum AdvertisementTypeEnum {
    PREMIUM("PREMIUM"), STANDARD("STANDARD"),  VIP("VIP")
    ;
    private String value;
    AdvertisementTypeEnum(String value) {
        this.value = value;
    }
}
