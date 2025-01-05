package com.ducbao.service_be.model.dto.request;

import com.ducbao.common.model.enums.CategoryEnums;
import lombok.Data;

import java.util.Set;

@Data
public class CategoryRequest {
    private String name;

    private CategoryEnums type;

    private String parentId;

    private String description;

    private Set<String> tags;
}
