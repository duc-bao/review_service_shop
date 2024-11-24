package com.ducbao.service_be.model.entity;

import com.ducbao.common.model.entity.FavoriteBaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@SuperBuilder
@Document(collection = "favorites")
public class FavoriteModel extends FavoriteBaseModel {
}
