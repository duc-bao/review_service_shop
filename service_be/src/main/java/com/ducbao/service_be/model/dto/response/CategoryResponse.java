package com.ducbao.service_be.model.dto.response;

import com.ducbao.common.model.enums.CategoryEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private String id;

    private String name;

    private CategoryEnums type;

    private String parentId;

    private String description;

    private boolean isDelete;

    private Set<String> tags;
}
