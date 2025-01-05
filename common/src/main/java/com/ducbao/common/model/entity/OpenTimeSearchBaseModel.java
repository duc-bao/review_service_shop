package com.ducbao.common.model.entity;

import com.ducbao.common.model.enums.DayOfWeekEnums;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
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
    private boolean isDayOff;
}
