package com.ducbao.service_be.model.dto.request;

import com.ducbao.common.anotation.IsEmail;
import com.ducbao.common.model.enums.CategoryEnums;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ShopRequest {
    @NotBlank(message = "Tên cửa hàng là bắt buộc")
    private String name;

    @NotBlank(message = "Avatar là trường bắt buộc")
    private String avatar;

    @NotBlank(message = "Ảnh giấy phép kinh doanh là bắt buộc")
    private String imageBusiness;

    @NotBlank(message = "Email là trường bắt buộc")
    @IsEmail
    private String email;

    private List<String> mediaUrls;

    private String description;

    private String urlWebsite;

    @NotBlank(message = "Thời gian mở cửa đóng cửa là bắt buộc")
    private List<OpenTimeRequest> openTimeRequests;

    private String city;

    private String ward;

    private String district;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private CategoryEnums categoryEnum;

    private String idCategory;

    private String phone;

    private boolean isOwner;
}
