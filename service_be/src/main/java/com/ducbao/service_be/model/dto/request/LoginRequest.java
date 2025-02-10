package com.ducbao.service_be.model.dto.request;

import com.ducbao.service_be.model.dto.response.UserInfoResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
