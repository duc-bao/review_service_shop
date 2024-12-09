package com.ducbao.common.model.entity;

import com.ducbao.common.model.enums.StateServiceEnums;
import com.ducbao.common.model.enums.StatusShopEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopSearchBaseModel {
    @Id
    private String id;

    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "standard"),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword),
                    @InnerField(suffix = "no_tone", type = FieldType.Text, analyzer = "no_tone_analyzer"),
                    @InnerField(suffix = "no_tone_nor", type = FieldType.Keyword, normalizer = "no_tone_normalizer"),
            }
    )
    private String name;

    @Field(type = FieldType.Text)
    private String avatar;

    @Field(type = FieldType.Text)
    private String email;

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

    @Field(type = FieldType.Text)
    private String city;

    @Field(type = FieldType.Text)
    private String ward;

    @Field(type = FieldType.Text)
    private String district;

    @Field(type = FieldType.Boolean)
    private boolean hasAnOwner;

    @Field(type = FieldType.Keyword)
    private StatusShopEnums statusShopEnums;

    @Field(type = FieldType.Keyword)
    private StateServiceEnums stateServiceEnums;

    @Field(type = FieldType.Nested)
    CategorySearchBaseModel categorySearchBaseModel;

    @Field(type = FieldType.Nested)
    List<OpenTimeSearchBaseModel> openTimeSearchBaseModels;
}
