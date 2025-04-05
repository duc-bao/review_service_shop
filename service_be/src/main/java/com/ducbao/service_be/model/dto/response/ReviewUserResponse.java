package com.ducbao.service_be.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewUserResponse {
    private String id;
    private String reviewTitle;

    private String reviewContent;

    private double rating;

    private List<String> mediaUrlReview;

    private int like;

    private int helpful;

    private int notLike;

    private String idService;

    private String idUser;

    private String idShop;

    private boolean isEdit;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Instant createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Instant updatedAt;

    private UserReviewInfo userReviewInfo;
}
