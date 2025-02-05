package com.ducbao.common.model.entity;

import com.ducbao.common.model.enums.DayOfWeekEnums;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenTimeSearchBaseModel {
    @Field(type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Keyword)
    private DayOfWeekEnums dayOfWeek;

    @Field(type = FieldType.Text)
    private String openTime;

    @Field(type = FieldType.Text)
    private String closeTime;

    @Field(type = FieldType.Boolean)
    @JsonProperty("isDayOff")
    private boolean isDayOff;
}
