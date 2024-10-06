package com.ducbao.common.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

@Data
@SuperBuilder
@NoArgsConstructor
public class ShopBaseModel extends BaseModel {
    @Id
    private String id;

    private String name;

    private String avatar;

    private String email;

    private boolean isVery;

    private String urlWebsite;

    private String idUser;
}
