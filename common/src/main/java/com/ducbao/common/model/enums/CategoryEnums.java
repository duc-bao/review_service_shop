package com.ducbao.common.model.enums;

public enum CategoryEnums {
    RESTAURANT(1), LAUNDRY(2), CUTTING(3),BEVERAGE(4), OTHER(5);
    ;
    private final int value;

    CategoryEnums(int value) {
        this.value = value;
    }
}
