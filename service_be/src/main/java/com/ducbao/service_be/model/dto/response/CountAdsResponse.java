package com.ducbao.service_be.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CountAdsResponse {
    private Integer total;
    private Integer totalCurrent;
    private Double totalPayment;
}
