package com.ducbao.service_be.model.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryCountRequest {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
