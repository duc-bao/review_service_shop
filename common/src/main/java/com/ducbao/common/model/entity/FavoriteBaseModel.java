package com.ducbao.common.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@SuperBuilder
public class FavoriteBaseModel  extends BaseModel{
    @Id
    private String id;

    private String idUser;

    private String idShop;
}
