package com.ducbao.service_be.model.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ShopUpdateRequest {
    private String description;

    private String city;

    private String ward;

    private String district;

    private List<String> mediaUrls;

    private String urlWebsite;

    private List<OpenTimeRequest> openTimeRequests;

    private String avatar;
}
