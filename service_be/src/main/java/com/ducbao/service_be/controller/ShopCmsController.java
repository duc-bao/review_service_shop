package com.ducbao.service_be.controller;

import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.service_be.model.dto.request.ShopDeactiveRequest;
import com.ducbao.service_be.model.dto.request.ShopTotalRequest;
import com.ducbao.service_be.model.dto.response.*;
import com.ducbao.service_be.service.ShopService;
import com.ducbao.service_be.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cms/shops")
@RolesAllowed(value = "ADMIN")
@RequiredArgsConstructor
@Slf4j
public class ShopCmsController {
    private final ShopService shopService;
    private final UserService userService;
    @Operation(
            summary = "Kích hoạt cửa hàng11122",
            description = "Api Kích hoạt cửa hàng ",
            tags = {"ADMIN:SHOPS"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "SHOP1000", description = "Kích hoạt cửa hàng thành công", content = {@Content(examples = @ExampleObject(value = """
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
                          statusCode: "SHOP1000"
                      }
                    """))}
            ),
    })
    @PutMapping("/active-shop/{id}")
    public ResponseEntity<ResponseDto<ShopResponse>> activateShop(@PathVariable(value = "id") String idShop) {
        return shopService.activeShop(idShop);
    }

    @Operation(
            summary = "Lấy thông tin cửa hàng với id",
            description = "Api Lấy thông tin cửa hàng với id ",
            tags = {"ADMIN:SHOPS"})
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

    @Operation(
            summary = "Lấy dịch vụ của cửa hàng theo id",
            description = "Api Lấy dịch vụ của cửa hàng theo id",
            tags = {"ADMIN:SHOPS"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "SERVICE1000", description = "Lấy dịch vụ của cửa hàng theo id", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy dịch vụ của cửa hàng theo id thành công",
                          "data": {
                                 "success": true,
                          "message": "Cập nhật dịch vụ thành công",
                          "data": {
                              "id": "6718c25e43333b7137e625f9",
                              "idShop": "6718c19c43333b7137e625f8",
                              "name": "CSCCS",
                              "type": null,
                              "description": "nhà hàng àlsclslcslcs",
                              "thumbnail": "âcsccscs",
                              "mediaUrl": [
                                  "accccccaaa"
                              ],
                              "idCategory": null,
                              "city": "HCM",
                              "ward": "Quận 1",
                              "district": "Phong vũ",
                              "countReview": 10,
                              "longitude": 10,
                              "latitude": 10,
                              "point": 10,
                              "price": 5000000
                          },
                          "statusCode": "SERVICE1000",
                          "meta": null
                          },
                          statusCode: "SERVICE1000"
                      }
                    """))}
            ),
    })
    @GetMapping("/service/{id}")
    public ResponseEntity<ResponseDto<ServiceResponse>> getServiceById(@PathVariable String id){
        return shopService.getServiceById(id);
    }

    @Operation(
            summary = "Lấy tất cả danh sách cửa hàng đã được kích hoạt hoặc chưa kích hoạt hoặc tất cả cửa hàng cho admin",
            description = "Api Lấy tất cả danh sách cửa hàng đã được kích hoạt hoặc chưa kích hoạt hoặc tất cả cửa hàng cho admin",
            tags = {"ADMIN:SHOPS"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "SHOP1000", description = "Lấy tất cả danh sách cửa hàng đã được kích hoạt hoặc chưa kích hoạt hoặc tất cả cửa hàng cho admin", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy danh sách cửa hàng chưa được kích hoạt thành công",
                          "data": {
                          "success": true,
                          "message": "Cập nhật dịch vụ thành công",
                          "data": {
                              "id": "6718c25e43333b7137e625f9",
                              "idShop": "6718c19c43333b7137e625f8",
                              "name": "CSCCS",
                              "type": null,
                              "description": "nhà hàng àlsclslcslcs",
                              "thumbnail": "âcsccscs",
                              "mediaUrl": [
                                  "accccccaaa"
                              ],
                              "idCategory": null,
                              "city": "HCM",
                              "ward": "Quận 1",
                              "district": "Phong vũ",
                              "countReview": 10,
                              "longitude": 10,
                              "latitude": 10,
                              "point": 10,
                              "price": 5000000
                          },
                          "statusCode": "SHOP1000",
                          "meta": null
                          },
                          statusCode: "SHOP1000"
                      }
                    """))}
            ),
    })
    @PostMapping("/list-shop-deactive")
    public ResponseEntity<ResponseDto<List<ShopResponse>>> getListShopDeactive(@RequestBody @Valid ShopDeactiveRequest request){
        log.info("getListShopDeactive() - {}", request);
        return shopService.getListShopDeActive(request);
    }

    @Operation(
            summary = "Lấy danh sách thời gian của cửa hàng",
            description = "Api Lấy danh sách thời gian của cửa hàng",
            tags = {"ADMIN:SHOPS"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "SERVICE1000", description = "Lấy danh sách thời gian của cửa hàng", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy danh sách thời gian của cửa hàng thành công",
                          "data": {
                             "id": "6718c25e43333b7137e625f9",
                             "idShop": "6718c19c43333b7137e625f8",
                             "name": "CSCCS",
                             "type": null,
                             "description": "nhà hàng àlsclslcslcs",
                             "thumbnail": "âcsccscs",
                             "mediaUrl": [
                                "accccccaaa"
                             ],
                             "idCategory": null,
                             "city": "HCM",
                             "ward": "Quận 1",
                             "district": "Phong vũ",
                             "countReview": 10,
                             "longitude": 10,
                             "latitude": 10,
                             "point": 10,
                             "price": 5000000
                          },
                          "statusCode": "SERVICE1000",
                          "meta": null
                          },
                      }
                    """))}
            ),
    })
    @GetMapping("/get-open-time/{id}")
    public ResponseEntity<ResponseDto<List<OpenTimeResponse>>> getListOpenTime(@PathVariable("id") String id){
        return shopService.getListOpenTime(id);
    }

    @Operation(
            summary = "Khóa cửa hàng này",
            description = "Api Khóa cửa hàng này",
            tags = {"ADMIN:SHOPS"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "SERVICE1000", description = "Khóa cửa hàng này", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Khóa cửa hàng thành công",
                          "data": {
                             "id": "6718c25e43333b7137e625f9",
                             "idShop": "6718c19c43333b7137e625f8",
                             "name": "CSCCS",
                             "type": null,
                             "description": "nhà hàng àlsclslcslcs",
                             "thumbnail": "âcsccscs",
                             "mediaUrl": [
                                "accccccaaa"
                             ],
                             "idCategory": null,
                             "city": "HCM",
                             "ward": "Quận 1",
                             "district": "Phong vũ",
                             "countReview": 10,
                             "longitude": 10,
                             "latitude": 10,
                             "point": 10,
                             "price": 5000000
                          },
                          "statusCode": "SERVICE1000",
                          "meta": null
                          },
                      }
                    """))}
            ),
    })
    @PutMapping("/block-shop/{id}")
    public ResponseEntity<ResponseDto<ShopResponse>> blockShop(@PathVariable("id") String id){
        return shopService.blockShop(id);
    }


}
