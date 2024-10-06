package com.ducbao.common.model.entity;

import com.ducbao.common.model.enums.DayOfWeekEnums;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class OpenTimeBaseModel extends BaseModel {
    private String id;

    private DayOfWeekEnums dayOfWeekEnum;

    private boolean isDayOff;

    private String openTime;

    private String closeTime;

}
