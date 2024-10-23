package com.ducbao.service_be.model.dto.request;

import com.ducbao.common.model.enums.StateServiceEnums;
import lombok.Data;

import java.util.List;

@Data
public class ServiceRequest {

    private String name;

    private String description;

    private String thumbnail;

    private List<String> mediaUrl;

    private StateServiceEnums stateService;

    private int countReview;

    private double point;

    private double price;
}
