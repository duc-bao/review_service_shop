package com.ducbao.service_be.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EmailResponse {
    private String messageId;
    private List<String> messageIds;
}
