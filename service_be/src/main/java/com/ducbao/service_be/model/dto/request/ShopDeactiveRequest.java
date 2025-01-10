package com.ducbao.service_be.model.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ShopDeactiveRequest {
    @Max(value = 100, message = "Keyword no more than 50 characters")
    private String keyword;
    @Min(value = 1, message = "Page must be greater than 1")
    private int page;
    @Min(value = 5, message = "Size must be greater than 5")
    private int size;
    private boolean isDeActive;
}
