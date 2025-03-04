package com.ducbao.service_be.model.dto.request;

import com.ducbao.common.model.enums.AdvertisementTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvertisementRequest {
    @NotBlank(message = "Tên chương trình quảng cáo không được để trống")
    private String name;
    private String description;
    private Integer price;
    private AdvertisementTypeEnum advertisementTypeEnum;
    @NotBlank(message = "Ảnh đại diện của gói quảng cáo không được để trống")
    private String thumbnail;
    @NotNull(message = "Không được để trống thời lượng của gói này")
    private LocalDateTime duration;
}
