package com.ducbao.common.model.entity;

import com.ducbao.common.model.enums.StateServiceEnums;
import com.ducbao.common.model.enums.StatusShopEnums;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ShopSearchBaseModel {
    @Id
    private String id;

    //    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "standard"),
//            otherFields = {
//                    @InnerField(suffix = "keyword", type = FieldType.Keyword),
//                    @InnerField(suffix = "no_tone", type = FieldType.Text, analyzer = "no_tone_analyzer"),
//                    @InnerField(suffix = "no_tone_nor", type = FieldType.Keyword, normalizer = "no_tone_normalizer"),
//            }
//    )
    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Text)
    private String avatar;

    @Field(type = FieldType.Text)
    private String email;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Text)
    private String createBy;

    @Field(type = FieldType.Boolean)
    private boolean isVery;

    @Field(type = FieldType.Text)
    private String urlWebsite;

    @Field(type = FieldType.Text)
    private String phoneNumber;

    @GeoPointField
    private GeoPoint location;

    @Field(type = FieldType.Text)
    private List<String> mediaUrls;

    @Field(type = FieldType.Integer)
    private Integer countReview;

    @Field(type = FieldType.Keyword)
    private String city;

    @Field(type = FieldType.Keyword)
    private String ward;

    @Field(type = FieldType.Keyword)
    private String district;

    @Field(type = FieldType.Boolean)
    private boolean hasAnOwner;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;

    @Field(type = FieldType.Double)
    private double point;

    @Field(type = FieldType.Keyword)
    private StatusShopEnums statusShopEnums;

    @Field(type = FieldType.Keyword)
    private StateServiceEnums stateServiceEnums;

    @Field(type = FieldType.Nested)
    CategorySearchBaseModel categorySearchBaseModel;

    @Field(type = FieldType.Nested)
    List<OpenTimeSearchBaseModel> openTimeSearchBaseModels;

    @Field(type = FieldType.Nested)
    List<ServiceSearchBaseModel> serviceSearchBaseModels;

    @Field(type = FieldType.Keyword)
    private Integer codeCity;
    @Field(type = FieldType.Keyword)
    private Integer codeDistrict;
    @Field(type = FieldType.Keyword)
    private Integer codeWard;
    @Field(type = FieldType.Integer)
    private Integer view;
    @Field(type = FieldType.Text)
    private String imageBusiness;
}
