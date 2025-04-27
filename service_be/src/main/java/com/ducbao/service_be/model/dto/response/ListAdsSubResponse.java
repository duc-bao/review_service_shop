package com.ducbao.service_be.model.dto.response;

import com.ducbao.common.model.enums.StatusAdvertisement;
import com.ducbao.common.model.enums.StatusPaymentEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListAdsSubResponse {
    private String id;
    private String idShop;
    private String idAdvertisement;
    private String idHistoryPayment;
    private LocalDateTime issuedAt;
    private LocalDateTime expiredAt;
    private StatusAdvertisement status;
    private StatusPaymentEnum statusPayment;
    private String vnpTxnRef;
    private Integer totalView;
    private Instant createdAt;
    private Instant updatedAt;
}
