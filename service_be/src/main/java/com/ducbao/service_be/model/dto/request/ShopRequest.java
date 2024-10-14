package com.ducbao.service_be.model.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ShopRequest {
    private String name;

    private String avatar;

    private String email;

    private List<String> mediaUrls;

    private String description;

    private String urlWebsite;
}
