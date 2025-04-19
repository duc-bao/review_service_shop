package com.ducbao.service_be.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ADSShopRequest {
    private String idAdvertisement;
    private Double amount;
}
