package com.ducbao.common.model.entity;

import com.ducbao.common.model.enums.StateServiceEnums;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
public class ServiceBaseModel extends BaseModel {
    @Id
    private String id;

    private String idShop;

    @Indexed(unique = true)
    private String name;

    private String type;

    private String description;

    private String thumbnail;

    private List<String> mediaUrl;

    private String idCategory;

    private StateServiceEnums stateService;

    private boolean hasAnOwner;

    private String city;

    private String ward;

    private String district;

    private int countReview;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private List<OpenTimeBaseModel> openTimeBaseModelList;

    private  double point;

    private double price;

}
