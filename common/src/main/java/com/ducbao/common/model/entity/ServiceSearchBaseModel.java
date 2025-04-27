package com.ducbao.common.model.entity;

import com.ducbao.common.model.enums.CategoryEnums;
import com.ducbao.common.model.enums.StateServiceEnums;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceSearchBaseModel {
    private String id;

    @Field(type = FieldType.Text, analyzer = "vietnamese_no_tone", searchAnalyzer = "vietnamese_no_tone")
    private String name;

    @Field(type = FieldType.Keyword)
    private String description;

    @Field(type = FieldType.Keyword)
    private String thumbnail;

    @Field(type = FieldType.Keyword)
    private List<String> mediaUrl;

    @Field(type = FieldType.Integer)
    private int countReview;

    @Field(type = FieldType.Double)
    private  double point;

    @Field(type = FieldType.Double)
    private double price;

    @Field(type = FieldType.Boolean)
    @JsonProperty("isDelete")
    private boolean isDelete;

    @Field(type = FieldType.Keyword)
    private String idShop;
}


