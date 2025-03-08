package com.ducbao.service_be.model.dto.response;

import com.ducbao.common.model.enums.StatusPaymentEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoryPaymentResponse {
    private String id;
    private String transactionId;
    private String idShop;
    private String idAds;
    private Integer totalAmount;
    private String status;
    private StatusPaymentEnum statusPayment;
    private String contentPayment;
    private String vnp_TransactionNo;
}
