package com.ducbao.service_be.model.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class VerifyShopRequest {
    private List<String> mediaUrls;
    private String avatar;
    private boolean isOwner;
    private String phoneNumber;
    private String urlWebsite;
}
