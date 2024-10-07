package com.ducbao.common.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@SuperBuilder
@NoArgsConstructor
public class ShopBaseModel extends BaseModel {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private String avatar;

    private String email;

    private boolean isVery;

    private String urlWebsite;

    private String idUser;
}
