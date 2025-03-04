package com.ducbao.common.model.entity;

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
    private String currency;
    private String paymentMethod;
    private String status;
}
