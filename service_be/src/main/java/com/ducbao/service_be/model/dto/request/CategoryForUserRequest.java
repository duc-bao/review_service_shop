package com.ducbao.service_be.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryForUserRequest {
    @NotNull
    @Size(min = 1, max = 50)
    private Set<String> tags;

    @NotBlank(message = "Thể loại cha không được để trống ")
    private String idParent;
}
