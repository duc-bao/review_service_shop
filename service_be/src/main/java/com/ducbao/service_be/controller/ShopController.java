package com.ducbao.service_be.controller;

import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.service_be.model.dto.request.*;
import com.ducbao.service_be.model.dto.response.*;
import com.ducbao.service_be.service.FileService;
import com.ducbao.service_be.service.OpenTimeService;
import com.ducbao.service_be.service.ShopService;
import com.ducbao.service_be.service.elk.ShopSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shops")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService shopService;
    private final ShopSearchService shopSearchService;
    private final OpenTimeService openTimeService;


    @Operation(
            summary = "Tìm kiếm danh sách cửa hàng",
            description = "Api Tìm kiếm danh sách cửa hàng",
            tags = {"USERS:SHOPS"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "SHOP1000", description = "Tìm kiếm danh sách cửa hàng", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Tìm kiếm cửa hàng thành công",
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
                                 "statusShopEnums": "DEACTIVE",
                                 "very": false,
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
    @PostMapping("/search")
    public ResponseEntity<ResponseDto<List<ShopSearchResponse>>> searchShops(@Valid @RequestBody ShopSearchRequest shopSearchRequest) {
        log.info("SearchShops start - {}", shopSearchRequest.toString());
        return shopSearchService.searchShopService(shopSearchRequest);
    }

    @Operation(
            summary = "Tạo cửa hàng với người dùng",
            description = "Api tạo cửa hàng ",
            tags = {"USERS:SHOPS"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "SHOP1000", description = "Tạo cửa hàng với người dùng", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Tạo cửa hàng với người dùng",
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
                                 "statusShopEnums": "DEACTIVE",
                                 "very": false,
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
    @PostMapping("/create-shop")
    @SecurityRequirements(value = {})
//    @PreAuthorize("hasAuthority('OWNER')")
    public ResponseEntity<ResponseDto<ShopResponse>> createShop(@RequestBody @Valid ShopRequest shopRequest) {
        return shopService.createShop(shopRequest);
    }


    //    @Operation(
//            summary = "Tải ảnh đại diện của cửa hàng lên hệ thống",
//            description = "Api tải ảnh của cửa hàng lên hệ thống",
//            tags = {"USERS:SHOPS"}
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "SHOP1002", description = "Tải ảnh của cửa hàng lên hệ thống", content = {@Content(examples = @ExampleObject(value = """
//                     {
//                    	"success": true,
//                    	"message": "Tải ảnh cửa hàng lên thành công",
//                    	"data": [
//                                     "http://res.cloudinary.com/dbk09oy6h/image/upload/v1728836118/IMAGE_USER/6704f957a77f0442b1e32a23/1728836117285.jpg.jpg",
//                                     "http://res.cloudinary.com/dbk09oy6h/image/upload/v1728836118/IMAGE_USER/6704f957a77f0442b1e32a23/1728836117285.jpg.jpg"
//                                 ],
//                    	"statusCode": "SHOP1002"
//                    }
//                    """))}
//            ),
//    })
//    @PutMapping(value = "upload-image-shop", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ResponseDto<String>> uploadImageShop(@RequestParam("file") MultipartFile file) {
//        return shopService.uploadImagme(file);
//    }
    @Operation(
            summary = "Tải ảnh đại diện của cửa hàng lên hệ thống",
            description = "Api tải ảnh của cửa hàng lên hệ thống",
            tags = {"USERS:SHOPS"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "SHOP1002", description = "Tải ảnh của cửa hàng lên hệ thống", content = {@Content(examples = @ExampleObject(value = """
                     {
                        "success": true,
                        "message": "Tải ảnh cửa hàng lên thành công",
                        "data": [
                                     "http://res.cloudinary.com/dbk09oy6h/image/upload/v1728836118/IMAGE_USER/6704f957a77f0442b1e32a23/1728836117285.jpg.jpg",
                                     "http://res.cloudinary.com/dbk09oy6h/image/upload/v1728836118/IMAGE_USER/6704f957a77f0442b1e32a23/1728836117285.jpg.jpg"
                                 ],
                        "statusCode": "SHOP1002"
                    }
                    """))}
            ),
    })
    @PutMapping(value = "/upload-image-shop", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirements(value = {})
    public ResponseEntity<ResponseDto<String>> uploadImageAvatar(@RequestParam("file") MultipartFile file, @RequestParam("email") String email) {
        log.info(file.getOriginalFilename().toString());
        return shopService.uploadAvatar(file, email);
    }

    @Operation(
            summary = "Tải nhiều ảnh của cửa hàng lên hệ thống",
            description = "Api tải nhiều ảnh của cửa hàng lên hệ thống",
            tags = {"USERS:SHOPS"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "SHOP1002", description = "Tải nhiều ảnh của cửa hàng lên hệ thống", content = {@Content(examples = @ExampleObject(value = """
                     {
                    	"success": true,
                    	"message": "Tải nhiều ảnh cửa hàng lên thành công",
                    	"data": "http://res.cloudinary.com/dbk09oy6h/image/upload/v1728836118/IMAGE_USER/6704f957a77f0442b1e32a23/1728836117285.jpg.jpg",
                    	"statusCode": "SHOP1002"
                    }
                    """))}
            ),
    })
    @PutMapping(value = "/upload-multiple-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirements(value = {})
    public ResponseEntity<ResponseDto<List<String>>> uploadMultiImageShop(@RequestPart("files") MultipartFile[] files, @RequestPart("email") String email) {
        return shopService.uploadMultipartFile(files, email);
    }

    @Operation(
            summary = "Lấy thông tin cửa hàng với id",
            description = "Api Lấy thông tin cửa hàng với id ",
            tags = {"USERS:SHOPS"})
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
            tags = {"USERS:SERVICE"})
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
    public ResponseEntity<ResponseDto<ServiceResponse>> getServiceById(@PathVariable String id) {
        return shopService.getServiceById(id);
    }

    @Operation(
            summary = "Tăng số lượng truy cập của cửa hàng",
            description = "Api tăng số lượng truy cập của cửa hàng",
            tags = {"USERS:SHOPS"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "SERVICE1000", description = "Tăng số lượng truy cập của cửa hàng", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Tăng số lượng truy cập của cửa hàng thành công",
                          "data": {
                              }
                          statusCode: "SHOP1000"
                      }
                    """))}
            ),
    })
    @PostMapping("/record-view")
    public ResponseEntity<ResponseDto<Void>> recordView(@RequestBody @Valid RecordViewRequest request) {
        log.info("recordView {}", request);
        return shopService.recordView(request);
    }
    @Operation(
            summary = "Lấy danh sách dịch vụ ",
            description = "Api Lấy danh sách dịch vụ ",
            tags = {"USERS:SERVICE"},
            parameters = {
                    @Parameter(name = "q", description = "Ô nhập từ tìm kiếm", required = false,
                            schema = @Schema(type = "string")),
                    @Parameter(name = "filter", description = "Điều kiện lọc cho tìm kiếm \n" +
                            "Ví dụ: {\"type\":\"RESTAURANT\"}",
                            required = false,
                            schema = @Schema(type = "string"
                            ))
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "SERVICE1002", description = "Lấy thành công danh sách dịch vụ của cửa hàng đó với từ khóa và lọc", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy thành công danh sách dịch vụ của cửa hàng đó với từ khóa và lọc",
                          "data": [
                             {
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
                                  }
                          ],
                          "statusCode": "SERVICE1002",
                          "meta": {
                              "total": 1,
                              "totalPage": 1,
                              "currentPage": 1,
                              "pageSize": 12
                          }
                      }
                    """))}
            ),
            @ApiResponse(
                    responseCode = "SERVICE1002", description = "Lấy thành công danh sách dịch vụ của cửa hàng đó với từ khóa ", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy danh sách danh mục với lọc thành công",
                          "data": [
                            {
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
                                  }
                          ],
                          "statusCode": "SERVICE1002",
                          "meta": {
                              "total": 1,
                              "totalPage": 1,
                              "currentPage": 1,
                              "pageSize": 12
                          }
                      }
                    """))}
            ),
            @ApiResponse(
                    responseCode = "SERVICE1002", description = "Lấy thành công danh sách dịch vụ của cửa hàng đó với lọc", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy thành công danh sách thể loại với nội dung tìm kiếm và lọc",
                          "data": [
                            {
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
                                  }
                          ],
                          "statusCode": "SERVICE1002",
                          "meta": {
                              "total": 1,
                              "totalPage": 1,
                              "currentPage": 1,
                              "pageSize": 12
                          }
                      }
                    """))}
            ),
    })
    @GetMapping("/service")
    public ResponseEntity<ResponseDto<List<ServiceResponse>>> getAllService(
            @RequestParam(value = "sort", defaultValue = "createAt") String s,
            @RequestParam(value = "limit", defaultValue = "12") int limit,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "keyword", required = false) String q,
            @RequestParam(value = "filter", required = false) String filter
    ) {
        return shopService.getAllService(limit, page, s, q, filter);
    }

    @SecurityRequirements({})
    @Operation(
            summary = "Lấy trung bình điểm số review của cửa hàng",
            description = "Api trung bình điểm số review của cửa hàng",
            tags = {"USERS:SHOPS"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "SERVICE1000", description = "Lấy trung bình điểm số review của cửa hàng", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy trung bình điểm số review của cửa hàng thành công",
                          "data": {
                            4.2
                          },
                          "statusCode": "SERVICE1000",
                          "meta": null
                          },
                      }
                    """))}
            ),
    })
    @GetMapping("/score/{id}")
    public ResponseEntity<ResponseDto<Double>> getScoreReview(@PathVariable("id") String id) {
        return shopService.getPointReview(id);
    }

    @Operation(
            summary = "Kích hoạt lại đúng chủ cửa hàng",
            description = "Api Kích hoạt lại đúng chủ cửa hàng",
            tags = {"USERS:SHOPS"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "SERVICE1000", description = "Kích hoạt lại đúng chủ cửa hàng", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Cập nhật chủ cửa hàng mới thành công vui lòng chờ admin xác nhận",
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
    @PutMapping("/update-active-shop/{id}")
    public ResponseEntity<ResponseDto<ShopResponse>> updateActiveShop(@PathVariable("id") String id, @RequestBody VerifyShopRequest verifyShopRequest) {
        return shopService.updateActiveShop(id, verifyShopRequest);
    }

    @Operation(
            summary = "Lấy danh sách thời gian của cửa hàng",
            description = "Api Lấy danh sách thời gian của cửa hàng",
            tags = {"USERS:SHOPS"})
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
    public ResponseEntity<ResponseDto<List<OpenTimeResponse>>> getListOpenTime(@PathVariable("id") String id) {
        return shopService.getListOpenTime(id);
    }

    @Operation(
            summary = "Lấy danh sách cửa hàng đề xuất",
            description = "Api Lấy danh sách cửa hàng đề xuất",
            tags = {"USERS:SHOPS"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "SERVICE1000", description = "Lấy danh sách cửa hàng đề xuất", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy danh sách cửa hàng đề xuất thành công",
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
    @PostMapping("/suggest")
    public ResponseEntity<ResponseDto<List<ShopSearchResponse>>> getSuggestShop(@RequestBody ShopSuggestRequest request) {
        log.info("GetSuggestShop() - {}", request.toString());
        return shopSearchService.suggestShopService(request);
    }
    @Operation(
            summary = "Lấy danh sách dịch vụ theo cửa hàng",
            description = "Api Lấy danh sách theo cửa hàng",
            tags = {"USERS:SHOPS"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "SERVICE1000", description = "Lấy danh sách theo cửa hàng", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy danh sách theo cửa hàng",
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
    @PostMapping("/list-service/{id}")
    public ResponseEntity<ResponseDto<List<ServiceResponse>>> getListServiceByIdShop(@PathVariable(value = "id") String id, @RequestBody PanigationRequest request){
        log.info("Get list service by id shop - {}" , id);
        return shopService.getListServiceById(id, request);
    }

    @Operation(
            summary = "Lấy chi tiết dịch vụ theo id",
            description = "Api Lấy chi tiết dịch vụ theo id",
            tags = {"USERS:SHOPS"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "SERVICE1000", description = "Lấy chi tiết dịch vụ theo id", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy chi tiết dịch vụ theo id",
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
    @GetMapping("/detail-service/{id}")
    public ResponseEntity<ResponseDto<ServiceResponse>> getDetailServiceByid(@PathVariable(value = "id") String id){
        log.info("Get detail service by id- {}" , id);
        return shopService.getDetailServiceById(id);
    }

    @Operation(
            summary = "Lấy chi tiết thời gian theo id",
            description = "Api Lấy chi tiết thời gian theo id",
            tags = {"USERS:SHOPS"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "SERVICE1000", description = "Lấy chi tiết thời gian theo id", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy chi tiết thời gian theo id",
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
    @GetMapping("/detail-opentime/{id}")
    public ResponseEntity<ResponseDto<OpenTimeResponse>> getDetailOpenTime(@PathVariable(value = "id") String id){
        log.info("Get detail open time by id- {}" , id);
        return openTimeService.getDetailById(id);
    }
}
