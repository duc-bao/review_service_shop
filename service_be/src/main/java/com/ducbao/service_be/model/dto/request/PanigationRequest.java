package com.ducbao.service_be.model.dto.request;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PanigationRequest {
    @Min(value = 1, message = "Page size must be at least 1")
    int limit;
    @Min(value = 0, message = "Page number must be at least 1")
    int page;
}
