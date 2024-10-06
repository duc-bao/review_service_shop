package com.ducbao.common.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

@Data
@SuperBuilder
@NoArgsConstructor
public class CommentBaseModel extends BaseModel{
    @Id
    private String id;

    private String content;

    private String idReview;

    private String idUser;
}
