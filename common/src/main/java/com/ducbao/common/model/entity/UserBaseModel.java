package com.ducbao.common.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
public class UserBaseModel extends BaseModel {
    @Id
    private String id;

    private String username;

    private String password;

    private String email;

    private String phone;

    private String city;

    private String ward;

    private String district;

    private int ratingUser;

    private List<String> role;

}
