package com.ducbao.service_be.model.dto.response;

import com.ducbao.common.model.enums.AdvertisementTypeEnum;
import com.ducbao.common.model.enums.StatusAdvertisement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvertisementResponse {
    private String id;
    private String name;
    private String description;
    private Integer price;
    private Integer total_access;
    private String thumbnail;
    private AdvertisementTypeEnum advertisementTypeEnum;
    private StatusAdvertisement statusAdvertisement;
    private Integer durationDay;
    private Instant createdAt;
    private Instant updatedAt;
}
