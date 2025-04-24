package com.ducbao.service_be.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListReviewResponse {
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

    private Instant createdAt;

    private Instant updatedAt;
}
