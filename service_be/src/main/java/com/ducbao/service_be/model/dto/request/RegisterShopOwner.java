package com.ducbao.service_be.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterShopOwner {
    @NotBlank(message = "Trường email là bắt buộc")
    private String email;

    @NotBlank(message = "Password là bắt buộc")
    private String password;
    @NotBlank(message = "Số điện thoại là bắt buộc")
    private String phone;
    private String city;
    private String ward;
    private String district;
}

