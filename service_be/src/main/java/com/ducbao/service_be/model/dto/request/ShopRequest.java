package com.ducbao.service_be.model.dto.request;

import com.ducbao.common.model.enums.CategoryEnums;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ShopRequest {
    @NotNull
    private String name;

    @NotNull
    private String avatar;

    private String imageBusiness;

    @NotNull
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

    private String idCategory;

    private String phone;

    private boolean isOwner;
}
