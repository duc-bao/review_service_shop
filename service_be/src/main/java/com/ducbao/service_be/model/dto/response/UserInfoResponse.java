package com.ducbao.service_be.model.dto.response;

import com.ducbao.common.model.enums.StatusUserEnums;
import lombok.Data;

@Data
public class UserInfoResponse {
    private String username;
    private String email;
    private String avatar;
    private StatusUserEnums statusUserEnums;
}
