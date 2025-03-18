package com.ducbao.service_be.model.dto.request;

import com.ducbao.common.model.enums.CategoryEnums;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

@Data
public class CategoryRequest {
    @NotBlank(message = "Tên thể loại không được để trống")
    private String name;

    private CategoryEnums type;

    private String parentId;

    private String description;

    @NotEmpty(message = "Các thẻ của thể loại không được để trống")
    private Set<String> tags;
}
