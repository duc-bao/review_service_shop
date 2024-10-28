package com.ducbao.common.model.enums;

public enum ReviewReactionType {
    LIKE("LIKE"), HELPFUL("HELPFUL"), NOTLIKE("NOTLIKE")
    ;
    private final String value;
    ReviewReactionType(String value) {
        this.value = value;
    }
}
