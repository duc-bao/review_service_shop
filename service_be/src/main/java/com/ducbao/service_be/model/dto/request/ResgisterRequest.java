package com.ducbao.service_be.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

@Data
public class ResgisterRequest {
    @NotNull(message = "Username là trường bắt buộc")
    private String username;

    @NotNull(message = "Password là trường bắt buộc")
    private String password;

    @NotNull(message = "Email là trường bắt buộc")
    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private String city;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Instant dateOfBirth;

    private String ward;

    private String district;
}
