package com.ducbao.service_be.model.dto.request;

import com.ducbao.common.model.enums.DayOfWeekEnums;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OpenTimeRequest {
    @NotNull(message = "Ngày trong tuần là bắt buộc")
    private DayOfWeekEnums dayOfWeekEnum;

    @NotNull(message = "Giờ mở cửa là bắt buộc")
    private String openTime;
    @NotNull(message = "Giờ đóng cửa là bắt buộc")
    private String closeTime;
    private boolean isDayOff;
}
