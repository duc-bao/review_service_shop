package com.ducbao.service_be.model.dto.request;

import com.ducbao.common.anotation.IsEmail;
import lombok.Data;

@Data
public class UserForgotPassword {
    @IsEmail
    private String email;
}
