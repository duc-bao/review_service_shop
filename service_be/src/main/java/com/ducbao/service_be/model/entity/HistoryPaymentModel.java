package com.ducbao.service_be.model.entity;

import com.ducbao.common.model.entity.HistoryPayementBaseModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "history_payments")
@Data
@NoArgsConstructor
@SuperBuilder
public class HistoryPaymentModel extends HistoryPayementBaseModel {
}
