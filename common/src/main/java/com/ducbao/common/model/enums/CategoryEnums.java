package com.ducbao.common.model.enums;

public enum CategoryEnums {
    RESTAURANT(1), HOME_SERVICE(2), BEAUTY_SPA(3),BEVERAGE(4), OTHER(5);
    ;
    private final int value;

    CategoryEnums(int value) {
        this.value = value;
    }
}
