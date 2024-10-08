package com.ducbao.service_be.model.dto.response;

import com.ducbao.common.model.enums.StatusUserEnums;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

@Data
public class UpdateProfileUserResponse {
    private String phone;

    private String city;

    private String avatar;

    private String firstName;

    private String lastName;

    private String ward;

    private String district;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Instant dateOfBirth;
}
