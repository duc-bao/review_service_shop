package com.ducbao.common.model.entity;

import com.ducbao.common.model.enums.StatusUserEnums;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
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

    private String avatar;

    private StatusUserEnums statusUserEnums;

    private String ward;

    private String district;

    private String firstName;

    private String lastName;

    private String activeCode;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Instant dateOfBirth;

    private int ratingUser;

    private List<String> role;

    private int quantityImage;

    private int notLike;

    private int helpful;

    private int like;
}
