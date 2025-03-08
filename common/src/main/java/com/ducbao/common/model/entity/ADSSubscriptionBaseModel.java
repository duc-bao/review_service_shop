package com.ducbao.common.model.entity;

import com.ducbao.common.model.enums.StatusAdvertisement;
import com.ducbao.common.model.enums.StatusPaymentEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@SuperBuilder
public class ADSSubscriptionBaseModel extends BaseModel {
    @Id
    private String id;
    private String idShop;
    private String idAdvertisement;
    private String idHistoryPayment;
    private LocalDateTime issuedAt;
    private LocalDateTime expiredAt;
    private StatusAdvertisement status;
    private StatusPaymentEnum statusPayment;
    private String vnpTxnRef;
}
