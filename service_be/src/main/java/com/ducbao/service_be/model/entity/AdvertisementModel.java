package com.ducbao.service_be.model.entity;

import com.ducbao.common.model.entity.AdvertisementBaseModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "advertisements")
@SuperBuilder
@NoArgsConstructor
@Data
public class AdvertisementModel extends AdvertisementBaseModel {
}
