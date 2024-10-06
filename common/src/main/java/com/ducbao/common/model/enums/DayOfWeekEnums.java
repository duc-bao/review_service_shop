package com.ducbao.common.model.enums;

public enum DayOfWeekEnums {
    MONDAY(0),
    TUESDAY(1),
    WEDNESDAY(2),
    THURSDAY(3),
    FRIDAY(4),
    SATURDAY(5),
    SUNDAY(6);
    ;

    private final int value;

    DayOfWeekEnums(int value) {
        this.value = value;
    }
}
