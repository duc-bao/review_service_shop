package com.ducbao.service_be.model.dto.request;

import com.ducbao.common.model.enums.StateServiceEnums;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ServiceRequest {

    @NotBlank(message = "Tên dịch vụ là bắt buộc")
    private String name;

    private String description;

    @NotBlank(message = "Ảnh avatar dịch vụ là bắt buộc")
    private String thumbnail;

    private List<String> mediaUrl;

    private StateServiceEnums stateService;

//    private int countReview;
//
//    private double point;

    private double price;
}
