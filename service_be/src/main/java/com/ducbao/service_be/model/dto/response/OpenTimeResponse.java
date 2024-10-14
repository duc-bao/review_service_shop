package com.ducbao.service_be.model.dto.response;

import com.ducbao.common.model.enums.DayOfWeekEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpenTimeResponse {
    private String id;
    private DayOfWeekEnums dayOfWeekEnum;
    private String openTime;
    private String closeTime;
    private boolean isDayOff;
}
