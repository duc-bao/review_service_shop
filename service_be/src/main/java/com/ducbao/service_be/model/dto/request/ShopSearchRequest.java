package com.ducbao.service_be.model.dto.request;

import com.ducbao.common.model.enums.SortOrderEnums;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopSearchRequest {
    private String keyword;
    private List<String> categoryId;
    private Double scoreReview;
    private String city;
    private String district;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String closeTime;
    private String sortField;
    private SortOrderEnums sortOrderEnums;
    @NotNull(message = "Page number is required")
    @Min(value = 0, message = "Page number must be non-negative")
    private Integer page;

    @NotNull(message = "Page size is required")
    @Min(value = 12, message = "Page size must be at least 1")
    private Integer size;
}
