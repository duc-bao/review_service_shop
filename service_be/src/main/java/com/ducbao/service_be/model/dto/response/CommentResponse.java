package com.ducbao.service_be.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    private String id;

    private String content;

    private String idReview;

    private String idUser;

    private String idShop;

    private boolean isComment;

    private boolean isEdit;
}
