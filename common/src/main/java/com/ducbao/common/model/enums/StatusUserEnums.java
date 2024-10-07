package com.ducbao.common.model.enums;

public enum StatusUserEnums {
    DEACTIVE(0), ACTIVE(1), DELETE(2), LOCK(3), NEED_LINK(4), WAIT_ACTIVE(5);

    public final int value;

    StatusUserEnums(int i) {
        value = i;
    }

}
