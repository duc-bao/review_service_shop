package com.ducbao.service_be.model.dto.response;

import com.ducbao.common.model.enums.StatusUserEnums;
import lombok.Data;

import java.util.List;

@Data
public class UserInfoResponse {
    private String username;
    private String email;
    private String avatar;

    private String firstName;

    private String lastName;
    private StatusUserEnums statusUserEnums;
    private List<String> role;
}
