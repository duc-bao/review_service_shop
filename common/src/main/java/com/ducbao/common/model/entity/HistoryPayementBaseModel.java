package com.ducbao.common.model.entity;

import com.ducbao.common.model.enums.StatusPaymentEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

@Data
@SuperBuilder
@NoArgsConstructor
public class HistoryPayementBaseModel extends BaseModel {
    @Id
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
