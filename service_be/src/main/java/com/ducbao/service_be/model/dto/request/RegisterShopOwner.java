package com.ducbao.service_be.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterShopOwner {
    private String email;
    private String password;
    private String phone;
    private String city;
    private String ward;
    private String district;
}

