package com.ducbao.common.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
public class ReviewBaseModel extends BaseModel {
    @Id
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
}
