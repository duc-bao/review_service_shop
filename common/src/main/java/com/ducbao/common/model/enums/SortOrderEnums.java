package com.ducbao.common.model.enums;

import co.elastic.clients.elasticsearch._types.SortOrder;
import lombok.Getter;

@Getter
public enum SortOrderEnums {
    ASC(SortOrder.Asc), DESC(SortOrder.Desc);
    private SortOrder sortOrder;
    SortOrderEnums(SortOrder value) {
        this.sortOrder = value;
    }
}
