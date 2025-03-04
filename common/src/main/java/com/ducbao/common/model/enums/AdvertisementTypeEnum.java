package com.ducbao.common.model.enums;

public enum AdvertisementTypeEnum {
    PREMIUM("Premium"), GOLD("GOLD"),  SLIVER("SLIVER")
    ;
    private String value;
    AdvertisementTypeEnum(String value) {
        this.value = value;
    }
}
