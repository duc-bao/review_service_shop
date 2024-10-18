package com.ducbao.service_be.model.dto.response;

import com.ducbao.common.model.entity.OpenTimeBaseModel;
import com.ducbao.common.model.enums.CategoryEnums;
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
public class ShopResponse {
    private String id;

    private String name;

    private String avatar;

    private String email;

    private boolean isVery;

    private String description;

    private String urlWebsite;

    private List<String> mediaUrls;

    private List<String> listIdOpenTime;

    private StatusShopEnums statusShopEnums;

    private String city;

    private String ward;

    private String district;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private CategoryEnums categoryEnum;
}
