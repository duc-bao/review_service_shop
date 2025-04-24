package com.ducbao.service_be.model.dto.response;

import com.ducbao.common.model.enums.StatusPaymentEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListHistoryResponse {
    private String id;
    private String transactionId;
    private String idShop;
    private String idAds;
    private Integer totalAmount;
    private String status;
    private StatusPaymentEnum statusPayment;
    private String contentPayment;
    private String vnp_TransactionNo;
    private Instant createdAt;
    private Instant updatedAt;
}
