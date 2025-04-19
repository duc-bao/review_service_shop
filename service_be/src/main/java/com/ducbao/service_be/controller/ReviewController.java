package com.ducbao.service_be.controller;

import com.ducbao.common.model.constant.SwaggerConstant;
import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.service_be.model.dto.request.ReviewGetAllRequest;
import com.ducbao.service_be.model.dto.request.ReviewReactionRequest;
import com.ducbao.service_be.model.dto.request.ReviewRequest;
import com.ducbao.service_be.model.dto.request.ReviewUpdateRequest;
import com.ducbao.service_be.model.dto.response.ReviewResponse;
import com.ducbao.service_be.model.dto.response.ReviewUserResponse;
import com.ducbao.service_be.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @SecurityRequirements({
            @SecurityRequirement(name = SwaggerConstant.NAME)
    })
    @Operation(
            summary = "Tạo đánh giá với cửa hàng hoặc dịch vụ của cửa hàng",
            description = "Api tạo đánh giá với cửa hàng hoặc dịch vụ của cửa hàng",
            tags = {"USERS:REVIEWS"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "REVIEW1000", description = "Tạo đánh giá với cửa hàng", content = @Content(examples = @ExampleObject(value = """
                     "success": true,
                     "message": "Tạo đánh giá của cửa hàng thành công",
                     "data": {
                         "id": "671a5ff76b7c1d48698c6d79",
                          "reviewTitle": "Review nhà hàng",
                          "reviewContent": "Nhà hàng ngon bổ rẻ",
                         "rating": 5,
                         "mediaUrlReview": [
                            "âccasccs"
                         ],
                         "like": 0,
                         "helpful": 0,
                         "notLike": 0,
                         "idService": null,
                         "idUser": "671a5ef76b7c1d48698c6d71",
                         "idShop": "670d0668c50bc3586429fdc1"
                     },
                     statusCode: "REVIEW1000"
                    """))),
            @ApiResponse(responseCode = "REVIEW1000", description = "Tạo đánh giá của dịch vụ của cửa hàng", content = @Content(examples = @ExampleObject(value = """
                     "success": true,
                     "message": "Tạo đánh giá của dịch vụ thành công",
                     "data": {
                          "id": "671a5ff76b7c1d48698c6d79",
                          "reviewTitle": "Review nhà hàng",
                          "reviewContent": "Nhà hàng ngon bổ rẻ",
                         "rating": 5,
                         "mediaUrlReview": [
                            "âccasccs"
                         ],
                         "like": 0,
                         "helpful": 0,
                         "notLike": 0,
                         "idService": 234a5ef76b7c1d4869acscc,
                         "idUser": "671a5ef76b7c1d48698c6d71",
                         "idShop": "670d0668c50bc3586429fdc1",
                         "createdAt": "2024-10-24T15:53:07.462Z",
                          "updatedAt": "2024-10-24T15:56:38.706Z",
                          "isEdit":false
                     },
                     statusCode: "REVIEW1000"
                    """))),
            @ApiResponse(responseCode = "REVIEW1001", description = "Xảy ra lỗi khi tạo dịch vụ", content = @Content(examples = @ExampleObject(value = """
                     "success": false,
                     "message": "Tạo đánh giá thất bại có lỗi xảy ra",
                     "data": {
                        null
                     },
                     statusCode: "REVIEW1001"
                    """)))
    })
    @PostMapping("")
    public ResponseEntity<ResponseDto<ReviewResponse>> createReview(@RequestBody @Valid ReviewRequest reviewRequest) {
        return reviewService.createReview(reviewRequest);
    }

    @SecurityRequirements({
            @SecurityRequirement(name = SwaggerConstant.NAME)
    })
    @Operation(
            summary = "Cập nhật đánh giá với cửa hàng hoặc dịch vụ của cửa hàng",
            description = "Api Cập nhật đánh giá với cửa hàng hoặc dịch vụ của cửa hàng",
            tags = {"USERS:REVIEWS"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "REVIEW1000", description = "Cập nhật đánh giá", content = @Content(examples = @ExampleObject(value = """
                     "success": true,
                     "message": "Cập nhật đánh giá thành công",
                     "data": {
                         "id": "671a5ff76b7c1d48698c6d79",
                          "reviewTitle": "Review nhà hàng",
                          "reviewContent": "Nhà hàng ngon bổ rẻ",
                         "rating": 5,
                         "mediaUrlReview": [
                            "âccasccs"
                         ],
                         "like": 0,
                         "helpful": 0,
                         "notLike": 0,
                         "idService": null,
                         "idUser": "671a5ef76b7c1d48698c6d71",
                         "idShop": "670d0668c50bc3586429fdc1",
                          "createdAt": "2024-10-24T15:53:07.462Z",
                          "updatedAt": "2024-10-24T15:56:38.706Z",
                         "isEdit": true
                     },
                     statusCode: "REVIEW1000"
                    """))),
            @ApiResponse(responseCode = "REVIEW1001", description = "Bạn không thể sửa đổi đánh giá này", content = @Content(examples = @ExampleObject(value = """
                     "success": false,
                     "message": "Bạn không thể sửa đổi đánh giá này",
                     "data": {
                        null
                     },
                     statusCode: "REVIEW1001"
                    """)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<ReviewResponse>> updateReview(@PathVariable("id") String id, @RequestBody ReviewUpdateRequest reviewRequest) {
        return reviewService.updateReview(reviewRequest, id);
    }

    @SecurityRequirements({
            @SecurityRequirement(name = SwaggerConstant.NAME)
    })
    @Operation(
            summary = "Xóa đánh giá",
            description = "Api Xóa đánh giá",
            tags = {"USERS:REVIEWS"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "REVIEW1004", description = "Xóa đánh giá", content = @Content(examples = @ExampleObject(value = """
                     "success": true,
                     "message": "Xóa đánh giá thành công",
                     "data": {
                     },
                     statusCode: "REVIEW1004"
                    """))),
            @ApiResponse(responseCode = "REVIEW1001", description = "Xóa đánh giá thất bại", content = @Content(examples = @ExampleObject(value = """
                     "success": false,
                     "message": "Xóa đánh giá thất bại",
                     "data": {
                        null
                     },
                     statusCode: "REVIEW1001"
                    """)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Void>> deleteReview(@PathVariable("id") String id) {
        return reviewService.deleteReview(id);
    }
    @SecurityRequirements({})
    @Operation(
            summary = "Lấy danh sách đánh giá của cửa hàng",
            description = "Api Lấy danh sách đánh giá của cửa hàng",
            tags = {"USERS:REVIEWS"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "REVIEW1000", description = "Lấy danh sách đánh giá của cửa hàng", content = @Content(examples = @ExampleObject(value = """
                     "success": true,
                     "message": "Lấy danh sách đánh giá theo cửa hàng thành công",
                     "data":[
                      {
                         "id": "671a5ff76b7c1d48698c6d79",
                          "reviewTitle": "Review nhà hàng",
                          "reviewContent": "Nhà hàng ngon bổ rẻ",
                         "rating": 5,
                         "mediaUrlReview": [
                            "âccasccs"
                         ],
                         "like": 0,
                         "helpful": 0,
                         "notLike": 0,
                         "idService": null,
                         "idUser": "671a5ef76b7c1d48698c6d71",
                         "idShop": "670d0668c50bc3586429fdc1"
                     },
                     ]
                     statusCode: "REVIEW1000",
                     "meta": {
                         "total": 1,
                         "totalPage": 1,
                         "currentPage": 1,
                         "pageSize": 12
                       }
                    """)))
    })
    @PostMapping("/getall/shop/{idShop}")
    public ResponseEntity<ResponseDto<List<ReviewUserResponse>>> findByShop(@RequestBody ReviewGetAllRequest request) {
        log.info("GetListReviewByIdShop - {}" , request.toString());
        return reviewService.getListReviewByIdShop(request);
    }

    @SecurityRequirements({})
    @Operation(
            summary = "Lấy danh sách đánh giá của dịch vụ",
            description = "Api Lấy danh sách đánh giá của dịch vụ",
            tags = {"USERS:REVIEWS"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "REVIEW1000", description = "Lấy danh sách đánh giá của dịch vụ", content = @Content(examples = @ExampleObject(value = """
                     "success": true,
                     "message": "Lấy danh sách đánh giá theo dịch vụ thành công",
                     "data":[
                      {
                         "id": "671a5ff76b7c1d48698c6d79",
                          "reviewTitle": "Review nhà hàng",
                          "reviewContent": "Nhà hàng ngon bổ rẻ",
                         "rating": 5,
                         "mediaUrlReview": [
                            "âccasccs"
                         ],
                         "like": 0,
                         "helpful": 0,
                         "notLike": 0,
                         "idService": null,
                         "idUser": "671a5ef76b7c1d48698c6d71",
                         "idShop": "670d0668c50bc3586429fdc1"
                     },
                     ]
                     statusCode: "REVIEW1000",
                     "meta": {
                         "total": 1,
                         "totalPage": 1,
                         "currentPage": 1,
                         "pageSize": 12
                       }
                    """)))
    })
    @GetMapping("/getall/service/{idService}")
    public ResponseEntity<ResponseDto<List<ReviewUserResponse>>> findByIdService(@PathVariable("idService") String idService,
                                                                                 @RequestParam(value = "limit", defaultValue = "12") int limit,
                                                                                 @RequestParam(value = "page", defaultValue = "1") int page,
                                                                                 @RequestParam(value = "sort", defaultValue = "updateAt") String sort) {
        return reviewService.getListReviewByIdService(idService, limit, page, sort);
    }

    @Operation(
            summary = "Lấy danh sách đánh giá theo tài khoản",
            description = "Api Lấy danh sách đánh giá theo tài khoản",
            tags = {"USERS:REVIEWS"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "REVIEW1000", description = "Lấy danh sách đánh giá theo tài khoản", content = @Content(examples = @ExampleObject(value = """
                     "success": true,
                     "message": "Lấy danh sách đánh giá theo tài khoản thành công",
                     "data":[
                      {
                         "id": "671a5ff76b7c1d48698c6d79",
                          "reviewTitle": "Review nhà hàng",
                          "reviewContent": "Nhà hàng ngon bổ rẻ",
                         "rating": 5,
                         "mediaUrlReview": [
                            "âccasccs"
                         ],
                         "like": 0,
                         "helpful": 0,
                         "notLike": 0,
                         "idService": null,
                         "idUser": "671a5ef76b7c1d48698c6d71",
                         "idShop": "670d0668c50bc3586429fdc1"
                     },
                     ]
                     statusCode: "REVIEW1000",
                     "meta": {
                         "total": 1,
                         "totalPage": 1,
                         "currentPage": 1,
                         "pageSize": 12
                       }
                    """)))
    })
    @GetMapping("/getall/users/{idUser}")
    public ResponseEntity<ResponseDto<List<ReviewUserResponse>>> findByIdUser(@PathVariable("idUser") String idService,
                                                                              @RequestParam(value = "limit", defaultValue = "12") int limit,
                                                                              @RequestParam(value = "page", defaultValue = "1") int page,
                                                                              @RequestParam(value = "sort", defaultValue = "updateAt") String sort) {
        return reviewService.getListReviewByIdUser(idService, limit, page, sort);
    }
    @SecurityRequirements({})
    @Operation(
            summary = "Lấy danh sách đánh giá gần đây",
            description = "Api Lấy  danh sách đánh giá gần đây",
            tags = {"USERS:REVIEWS"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "REVIEW1000", description = "Lấy  danh sách đánh giá gần đây", content = @Content(examples = @ExampleObject(value = """
                     "success": true,
                     "message": "Lấy  danh sách đánh giá gần đây thành công",
                     "data":[
                      {
                         "id": "671a5ff76b7c1d48698c6d79",
                          "reviewTitle": "Review nhà hàng",
                          "reviewContent": "Nhà hàng ngon bổ rẻ",
                         "rating": 5,
                         "mediaUrlReview": [
                            "âccasccs"
                         ],
                         "like": 0,
                         "helpful": 0,
                         "notLike": 0,
                         "idService": null,
                         "idUser": "671a5ef76b7c1d48698c6d71",
                         "idShop": "670d0668c50bc3586429fdc1"
                     },
                     ]
                     statusCode: "REVIEW1000",
                     "meta": {
                         "total": 1,
                         "totalPage": 1,
                         "currentPage": 1,
                         "pageSize": 12
                       }
                    """)))
    })
    @GetMapping("/getall/recently")
    public ResponseEntity<ResponseDto<List<ReviewUserResponse>>> getAllReviewRecently(@RequestParam(value = "limit", defaultValue = "8") int limit,
                                                                                      @RequestParam(value = "page", defaultValue = "1") int page,
                                                                                      @RequestParam(value = "sort", defaultValue = "updateAt") String sort) {
        return reviewService.getListReviewRecently(limit, page, sort);
    }

    @Operation(
            summary = "Cập nhật cảm xúc của đánh giá ",
            description = "Api Cập nhật cảm xúc của đánh giá ",
            tags = {"USERS:REVIEWS"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "REVIEW1000", description = "Cập nhật cảm xúc của đánh giá ", content = @Content(examples = @ExampleObject(value = """
                     "success": true,
                     "message": "Cập nhật cảm xúc thành công",
                     "data":[
                      {
                         "reviewTitle": "Nhà hàng không ngon nhé",
                         "reviewContent": "Nhà hàng không ngon nhé mọi người",
                         "rating": 1,
                         "mediaUrlReview": [
                              "áccscscs"
                         ],
                         "like": 5,
                         "helpful": 0,
                         "notLike": 0,
                         "idService": "6718c25e43333b7137e625f9",
                         "idUser": "671a5ef76b7c1d48698c6d71",
                         "idShop": "6718c19c43333b7137e625f8",
                         "createdAt": "2024-10-24T15:53:07.462Z",
                         "updatedAt": "2024-10-26T11:04:37.349Z",
                         " userReviewInfo": {
                             "id": "671a5ef76b7c1d48698c6d71",
                             "city": "Lao Cai",
                             "avatar": null,
                             "ward": "Hanh Phuc",
                             "district": "Hong Ha",
                             "firstName": "bao",
                             "lastName": "anh duc",
                             "ratingUser": 3,
                             "quantityImage": 3,
                             "like": 5,
                             "helpful": 0,
                             "notLike": 0
                         },
                         "edit": true
                     },
                     ]
                     statusCode: "REVIEW1000",
                    """)))
    })
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/update-reaction/{id}")
    public ResponseEntity<ResponseDto<ReviewUserResponse>> updateReaction(@PathVariable("id") String idReview, @RequestBody ReviewReactionRequest reviewReactionRequest) {
        return reviewService.updateReviewLike(idReview, reviewReactionRequest);
    }
}

