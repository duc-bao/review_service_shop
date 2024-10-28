package com.ducbao.service_be.model.dto.response;

import com.ducbao.common.model.enums.StatusUserEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserReviewInfo {
    private String id;

    private String city;

    private String avatar;

    private String ward;

    private String district;

    private String firstName;

    private String lastName;

    private int ratingUser;

    private int quantityImage;

    private int like;

    private int helpful;

    private int notLike;
}
