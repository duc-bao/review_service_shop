package com.ducbao.service_be.controller;

import com.ducbao.common.model.constant.SwaggerConstant;
import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.service_be.model.dto.request.ReviewRequest;
import com.ducbao.service_be.model.dto.request.ReviewUpdateRequest;
import com.ducbao.service_be.model.dto.response.ReviewResponse;
import com.ducbao.service_be.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            tags = {"users:reviews"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "REVIEW1000",description = "Tạo đánh giá với cửa hàng", content = @Content(examples = @ExampleObject(value = """
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
            @ApiResponse(responseCode = "REVIEW1000",description = "Tạo đánh giá của dịch vụ của cửa hàng", content = @Content(examples = @ExampleObject(value = """
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
            @ApiResponse(responseCode = "REVIEW1001",description = "Xảy ra lỗi khi tạo dịch vụ", content = @Content(examples = @ExampleObject(value = """
                     "success": false,
                     "message": "Tạo đánh giá thất bại có lỗi xảy ra",
                     "data": {
                        null
                     },
                     statusCode: "REVIEW1001"
                    """)))
    })
    @PostMapping("")
    public ResponseEntity<ResponseDto<ReviewResponse>> createReview(@RequestBody ReviewRequest reviewRequest) {
        return reviewService.createReview(reviewRequest);
    }

    @SecurityRequirements({
            @SecurityRequirement(name = SwaggerConstant.NAME)
    })
    @Operation(
            summary = "Cập nhật đánh giá với cửa hàng hoặc dịch vụ của cửa hàng",
            description = "Api Cập nhật đánh giá với cửa hàng hoặc dịch vụ của cửa hàng",
            tags = {"users:reviews"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "REVIEW1000",description = "Cập nhật đánh giá", content = @Content(examples = @ExampleObject(value = """
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
            @ApiResponse(responseCode = "REVIEW1001",description = "Bạn không thể sửa đổi đánh giá này", content = @Content(examples = @ExampleObject(value = """
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
            tags = {"users:reviews"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "REVIEW1004",description = "Xóa đánh giá", content = @Content(examples = @ExampleObject(value = """
                     "success": true,
                     "message": "Xóa đánh giá thành công",
                     "data": {
                     },
                     statusCode: "REVIEW1004"
                    """))),
            @ApiResponse(responseCode = "REVIEW1001",description = "Xóa đánh giá thất bại", content = @Content(examples = @ExampleObject(value = """
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
}

