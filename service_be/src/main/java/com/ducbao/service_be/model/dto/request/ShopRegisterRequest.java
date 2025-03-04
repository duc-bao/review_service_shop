package com.ducbao.service_be.model.dto.request;

import com.ducbao.common.anotation.IsEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopRegisterRequest {

    @NotBlank(message = "Email không được để trống")
    @IsEmail
    private String email;
    private String password;
}
