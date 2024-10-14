package com.ducbao.service_be.model.dto.response;

import com.ducbao.common.model.enums.StatusShopEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopGetResponse {
    private String id;

    private String name;

    private String avatar;

    private String email;

    private boolean isVery;

    private String description;

    private String urlWebsite;

    private List<String> mediaUrls;

    private List<OpenTimeResponse> listOpenTimes;

    private StatusShopEnums statusShopEnums;

    private String city;

    private String ward;

    private String district;

    private BigDecimal longitude;

    private BigDecimal latitude;
}
