package com.ducbao.service_be.model.dto.response;

import com.ducbao.common.model.enums.CategoryEnums;
import com.ducbao.common.model.enums.StateServiceEnums;
import com.ducbao.common.model.enums.StatusShopEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopSearchResponse {
    private String id;

    private String name;

    private String avatar;

    private String email;

    private boolean isVery;

    private String description;

    private String urlWebsite;

    private List<String> mediaUrls;

    private StatusShopEnums statusShopEnums;

    private String city;

    private String ward;

    private String district;

    private CategoryEnums categoryEnum;

    private StateServiceEnums stateService;

    private CategoryResponse categoryResponse;

    private boolean isDelete;

    private List<ServiceResponse> serviceResponses;

    private List<OpenTimeResponse> openTimeResponses;
}
