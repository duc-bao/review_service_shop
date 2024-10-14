package com.ducbao.service_be.controller;

import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.service_be.model.dto.request.ShopRequest;
import com.ducbao.service_be.model.dto.response.ShopResponse;
import com.ducbao.service_be.service.FileService;
import com.ducbao.service_be.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/shops")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService shopService;

    @Operation(
            summary = "Tạo cửa hàng với người dùng",
            description = "Api tạo cửa hàng ",
            tags = {"users:shops"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "SHOP1000", description = "Tạo cửa hàng với người dùng", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Tạo cửa hàng với người dùng",
                          "data": {
                                 "id": "670bf80e50f093201abbb571",
                                 "name": "Nhà hàng của Bảo",
                                 "avatar": "http://res.cloudinary.com/dbk09oy6h/image/upload/v1728836118/IMAGE_USER/6704f957a77f0442b1e32a23/1728836117285.jpg.jpg",
                                 "email": "truongducbao2904@gmail.com",
                                 "mediaUrls": [
                                     "http://res.cloudinary.com/dbk09oy6h/image/upload/v1728836118/IMAGE_USER/6704f957a77f0442b1e32a23/1728836117285.jpg.jpg",
                                     "http://res.cloudinary.com/dbk09oy6h/image/upload/v1728836118/IMAGE_USER/6704f957a77f0442b1e32a23/1728836117285.jpg.jpg"
                                 ],
                                 "description": "Nhà hàng món ăn hàng đầu về chất lượng",
                                 "urlWebsite": "https://quannhautudo.com/bai-viet/quan-nhau-chill-ha-noi-163.htm",
                                 "statusShopEnums": "DEACTIVE",
                                 "very": false
                          },
                          statusCode: "SHOP1000"
                      }
                    """))}
            ),
    })
    @PostMapping("/create-shop")
    public ResponseEntity<ResponseDto<ShopResponse>> createShop(@RequestBody ShopRequest shopRequest) {
        return shopService.createShop(shopRequest);
    }

    @Operation(
            summary = "Tải ảnh của cửa hàng lên hệ thống",
            description = "Api tải ảnh của cửa hàng lên hệ thống",
            tags = {"users:shops"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "SHOP1002", description = "Tải ảnh của cửa hàng lên hệ thống", content = {@Content(examples = @ExampleObject(value = """
                     {
                    	"success": true,
                    	"message": "Tải ảnh cửa hàng lên thành công",
                    	"data": "http://res.cloudinary.com/dbk09oy6h/image/upload/v1728836118/IMAGE_USER/6704f957a77f0442b1e32a23/1728836117285.jpg.jpg",
                    	"statusCode": "SHOP1002"
                    }
                    """))}
            ),
    })
    @PutMapping(value = "upload-image-shop", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<String>> uploadImageShop(MultipartFile file) {
        return shopService.uploadImagme(file);
    }
}
