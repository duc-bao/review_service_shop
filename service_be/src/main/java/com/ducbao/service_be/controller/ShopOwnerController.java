package com.ducbao.service_be.controller;

import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.service_be.model.dto.request.ShopRequest;
import com.ducbao.service_be.model.dto.response.ShopGetResponse;
import com.ducbao.service_be.model.dto.response.ShopResponse;
import com.ducbao.service_be.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/own/shop")
@Slf4j
public class ShopOwnerController {
    private final ShopService shopService;

    @Operation(
            summary = "Cập nhật cửa hàng",
            description = "Api cập nhật cửa hàng",
            tags = {"own:shops"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "SHOP1000", description = "Cập nhật cửa hàng thành công", content = @Content(examples = @ExampleObject(value = """
                    {
                    "success": true,
                    "message": "Kích hoạt cửa hàng thành công",
                    "data": {
                       "id": "670d0668c50bc3586429fdc1",
                       "name": "Nhà hàng của Bảo",
                       "avatar": "http://res.cloudinary.com/dbk09oy6h/image/upload/v1728836118/IMAGE_USER/6704f957a77f0442b1e32a23/1728836117285.jpg.jpg",
                       "email": "truongducbao2904@gmail.com",
                       "mediaUrls": [
                             "http://res.cloudinary.com/dbk09oy6h/image/upload/v1728836118/IMAGE_USER/6704f957a77f0442b1e32a23/1728836117285.jpg.jpg",
                             "http://res.cloudinary.com/dbk09oy6h/image/upload/v1728836118/IMAGE_USER/6704f957a77f0442b1e32a23/1728836117285.jpg.jpg"
                       ],
                       "description": "Nhà hàng món ăn hàng đầu về chất lượng",
                       "urlWebsite": "https://quannhautudo.com/bai-viet/quan-nhau-chill-ha-noi-163.htm",
                       "statusShopEnums": "ACTIVE",
                       "very": true,
                       "listIdOpenTime": [
                             "670d04f24e531645e73f8984",
                             "670d04f24e531645e73f8985",
                             "670d04f24e531645e73f8986"
                       ],
                        },
                          statusCode: "SHOP1000"}
                    """)))
    })
    @PutMapping("/update-shop")
    public ResponseEntity<ResponseDto<ShopResponse>> updateShop(@RequestBody ShopRequest shopRequest) {
        return shopService.updateShop(shopRequest);
    }

    @Operation(
            summary = "Lấy thông tin cửa hàng với id",
            description = "Api Lấy thông tin cửa hàng với id ",
            tags = {"own:shops"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "SHOP1000", description = "Lấy thông tin cửa hàng với id", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy thông tin cửa hàng theo id thành công",
                          "data": {
                                 "id": "670d0668c50bc3586429fdc1",
                                 "name": "Nhà hàng của Bảo",
                                 "avatar": "http://res.cloudinary.com/dbk09oy6h/image/upload/v1728836118/IMAGE_USER/6704f957a77f0442b1e32a23/1728836117285.jpg.jpg",
                                 "email": "truongducbao2904@gmail.com",
                                 "mediaUrls": [
                                     "http://res.cloudinary.com/dbk09oy6h/image/upload/v1728836118/IMAGE_USER/6704f957a77f0442b1e32a23/1728836117285.jpg.jpg",
                                     "http://res.cloudinary.com/dbk09oy6h/image/upload/v1728836118/IMAGE_USER/6704f957a77f0442b1e32a23/1728836117285.jpg.jpg"
                                 ],
                                 "description": "Nhà hàng món ăn hàng đầu về chất lượng",
                                 "urlWebsite": "https://quannhautudo.com/bai-viet/quan-nhau-chill-ha-noi-163.htm",
                                 "statusShopEnums": "ACTIVE",
                                 "very": true,
                                 "listIdOpenTime": [
                                       "670d04f24e531645e73f8984",
                                       "670d04f24e531645e73f8985",
                                       "670d04f24e531645e73f8986"
                                 ],
                          },
                          statusCode: "SHOP1000"
                      }
                    """))}
            ),
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<ShopGetResponse>> getShopById(@PathVariable("id") String id) {
        return shopService.getShopById(id);
    }
}
