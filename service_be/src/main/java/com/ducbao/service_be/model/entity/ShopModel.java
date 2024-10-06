package com.ducbao.service_be.model.entity;

import com.ducbao.common.model.entity.ShopBaseModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "shops")
@SuperBuilder
@NoArgsConstructor
public class ShopModel extends ShopBaseModel {
}
