package com.ducbao.service_be.model.dto.request;

import com.ducbao.common.model.enums.CategoryEnums;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ShopRequest {
    private String name;

    private String avatar;

    private String email;

    private List<String> mediaUrls;

    private String description;

    private String urlWebsite;

    private List<OpenTimeRequest> openTimeRequests;

    private String city;

    private String ward;

    private String district;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private CategoryEnums categoryEnum;
}
