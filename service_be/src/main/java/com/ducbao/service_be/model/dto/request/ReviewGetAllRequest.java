package com.ducbao.service_be.model.dto.request;

import lombok.Data;

@Data
public class ReviewGetAllRequest {
    private String idShop;
    private String keyword;
    private Integer page;
    private Integer size;
    private String sort;
    private Integer filter;
}
