package com.ducbao.service_be.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryTagsRequest {
    @NotNull
    private String idCategory;
    private Set<String> tags;
    private boolean isDelete;
}
