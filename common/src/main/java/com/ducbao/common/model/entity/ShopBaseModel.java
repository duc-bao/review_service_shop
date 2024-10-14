package com.ducbao.common.model.entity;

import com.ducbao.common.model.enums.StatusShopEnums;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
public class ShopBaseModel extends BaseModel {
    @Id
    private String id;

    private String name;

    private String avatar;

    private List<String> mediaUrls;

    private String email;

    private boolean isVery;

    private String description;

    private String urlWebsite;

    private String idUser;

    private StatusShopEnums statusShopEnums;
}
