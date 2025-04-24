package com.ducbao.service_be.model.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShopTotalRequest {
    LocalDateTime startDate;
    LocalDateTime endDate;
}
