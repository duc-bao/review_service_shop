package com.ducbao.service_be.model.dto.response;

import com.ducbao.common.model.entity.OpenTimeBaseModel;
import com.ducbao.common.model.enums.CategoryEnums;
import com.ducbao.common.model.enums.StateServiceEnums;
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
public class ServiceResponse {
    private String id;

    private String idShop;

    private String name;

    private CategoryEnums type;

    private String description;

    private String thumbnail;

    private List<String> mediaUrl;

    private String idCategory;

    private int countReview;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private  double point;

    private double price;

    private StateServiceEnums stateService;

}
