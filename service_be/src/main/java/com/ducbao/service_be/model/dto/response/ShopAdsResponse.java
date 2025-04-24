package com.ducbao.service_be.model.dto.response;

import com.ducbao.common.model.enums.CategoryEnums;
import com.ducbao.common.model.enums.StateServiceEnums;
import com.ducbao.common.model.enums.StatusShopEnums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopAdsResponse {
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

    private StateServiceEnums stateService;

    private String idCategory;

    private boolean isDelete;

    private Integer total_view;

    private Integer point;
    private Integer countReview;
    private Integer codeCity;
    private Integer codeDistrict;
    private Integer codeWard;
    private String imageBusiness;

    private String idAdvertisement;
}
