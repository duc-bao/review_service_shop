package com.ducbao.service_be.model.dto.response;

import com.ducbao.common.model.enums.StatusUserEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String id;

    private String username;

    private String email;

    private String phone;

    private String city;

    private String avatar;

    private String ward;

    private String district;

    private String firstName;

    private String lastName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Instant dateOfBirth;

    private int ratingUser;

    private int quantityImage;

    private int notLike;

    private int helpful;

    private int like;

    private StatusUserEnums statusUserEnums;
    private Integer codeCity;
    private Integer codeDistrict;
    private Integer codeWard;

    private Instant createdAt;
    private Instant updatedAt;
}
