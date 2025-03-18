package com.ducbao.service_be.model.dto.response;

import com.ducbao.common.model.enums.StatusAdvertisement;
import com.ducbao.common.model.enums.StatusPaymentEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdsSubcriptionResponse {
    private String id;
    private String idShop;
    private String idAdvertisement;
    private String idHistoryPayment;
    private LocalDateTime issuedAt;
    private LocalDateTime expiredAt;
    private StatusAdvertisement status;
    private StatusPaymentEnum statusPayment;
    private String vnpTxnRef;
    private String name;
    private String description;
    private Integer totalAccess;
    private String statusAds;
    private LocalDateTime createdAt;
    private Long remainingDay;
}
