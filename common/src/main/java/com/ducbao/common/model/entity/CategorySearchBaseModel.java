package com.ducbao.common.model.entity;

import com.ducbao.common.model.enums.CategoryEnums;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Set;

@Data
@Builder
public class CategorySearchBaseModel {
    @Field(type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Keyword)
    private String idParent;

    @Field(type = FieldType.Boolean)
    private boolean isDelete;

    @Field(type = FieldType.Keyword)
    private CategoryEnums categoryEnum;

    @Field(type = FieldType.Keyword)
    private Set<String> tags;
}
