package com.ducbao.service_be.model.dto.request;

import com.ducbao.common.model.enums.DayOfWeekEnums;
import lombok.Data;

@Data
public class OpenTimeRequest {
    private DayOfWeekEnums dayOfWeekEnum;
    private String openTime;
    private String closeTime;
    private boolean isDayOff;
}
