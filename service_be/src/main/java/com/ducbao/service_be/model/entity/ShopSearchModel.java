package com.ducbao.service_be.model.entity;

import com.ducbao.common.model.entity.ShopSearchBaseModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "shop")
@Data
@NoArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShopSearchModel extends ShopSearchBaseModel {
}
