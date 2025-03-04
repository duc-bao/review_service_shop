package com.ducbao.common.model.entity;

import com.ducbao.common.model.enums.AdvertisementTypeEnum;
import com.ducbao.common.model.enums.StatusAdvertisement;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public class AdvertisementBaseModel extends BaseModel{
    @Id
    private String id;

    private String name;

    private String description;

    private String idShop;

    private Integer price;

    private StatusAdvertisement statusAdvertisement;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Integer totalAccess;

    private AdvertisementTypeEnum advertisementTypeEnum;

    private String thumbnail;

    private LocalDateTime duration;
}
