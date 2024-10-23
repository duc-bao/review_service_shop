package com.ducbao.service_be.model.dto.request;

import com.ducbao.common.model.enums.StatusUserEnums;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

@Data
public class UserRequest {
    private String phone;

    private String city;

    private String avatar;

    private String ward;

    private String district;

    private String firstName;

    private String lastName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Instant dateOfBirth;

}
