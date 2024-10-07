package com.ducbao.service_be.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String accessToken;
    private UserInfoResponse userInfoResponse;
}
