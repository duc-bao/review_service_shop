package com.ducbao.service_be.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ReviewRequest {


    @NotNull()
    private String reviewTitle;

    @NotNull(message = "Nội dung đánh giá là bắt buộc")
    private String reviewContent;

    @NotBlank(message = "Điểm đánh giá là bắt buộc")
    @Min(value = 1, message = "Điểm đánh giá thấp nhất là 1")
    private double rating;

    private List<String> mediaUrlReview;

    private String idService;

    private String idShop;
}
