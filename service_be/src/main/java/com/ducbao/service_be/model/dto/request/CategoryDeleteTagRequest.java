package com.ducbao.service_be.model.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CategoryDeleteTagRequest {
    private String idCategory;
    private List<String> tags;
}
