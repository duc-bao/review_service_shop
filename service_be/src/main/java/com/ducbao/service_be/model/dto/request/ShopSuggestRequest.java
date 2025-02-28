package com.ducbao.service_be.model.dto.request;

import com.ducbao.common.model.enums.SortOrderEnums;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopSuggestRequest {
    private String sortField;
    private SortOrderEnums sortOrderEnums;

    @Min(value = 0, message = "Page number must be non-negative")
    @NotNull(message = "Page number is required")
    int page;
    @NotNull(message = "Size number is required")
    @Min(value = 5, message = "Page size must be at least 1")
    int size;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private String idShop;


}
