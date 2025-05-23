package com.ducbao.service_be.model.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ReviewUpdateRequest {
    private String reviewTitle;

    private String reviewContent;

    private double rating;

    private List<String> mediaUrlReview;
}
