package com.ducbao.service_be.model.dto.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

@Data
public class ResgisterRequest {
    private String username;

    private String password;

    private String email;

    private String phone;

    private String city;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Instant dateOfBirth;

    private String ward;

    private String district;
}
