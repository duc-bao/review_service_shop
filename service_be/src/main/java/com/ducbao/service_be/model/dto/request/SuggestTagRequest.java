package com.ducbao.service_be.model.dto.request;

import lombok.Data;

@Data
public class SuggestTagRequest {
    private String idCategory;
    private String keyword;
}
