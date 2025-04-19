package com.ducbao.service_be.model.dto.request;

import com.ducbao.common.model.enums.DayOfWeekEnums;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OpenTimeRequest {
    @NotNull(message = "Ngày trong tuần là bắt buộc")
    private DayOfWeekEnums dayOfWeekEnum;

    private String openTime;
    private String closeTime;
    private boolean isDayOff;
}
