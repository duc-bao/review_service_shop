package com.ducbao.service_be.model.entity;

import com.ducbao.common.model.entity.ADSSubscriptionBaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@SuperBuilder
@Document(collection = "ads_subscription")
public class ADSSubscriptionModel extends ADSSubscriptionBaseModel {
}
