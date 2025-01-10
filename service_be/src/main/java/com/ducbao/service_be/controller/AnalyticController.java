package com.ducbao.service_be.controller;

import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.service_be.model.dto.request.CategoryCountRequest;
import com.ducbao.service_be.model.dto.request.ShopTotalRequest;
import com.ducbao.service_be.model.dto.response.CountResponse;
import com.ducbao.service_be.service.CategoryService;
import com.ducbao.service_be.service.ReviewService;
import com.ducbao.service_be.service.ShopService;
import com.ducbao.service_be.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analytic")
@RolesAllowed(value = "ADMIN")
@RequiredArgsConstructor
@Slf4j
public class AnalyticController {
    private final UserService userService;
    private final ShopService shopService;
    private final CategoryService categoryService;
    private final ReviewService reviewService;


    @Operation(
            summary = "Tổng số cửa hàng trong thời gian",
            description = "Api Tổng số cửa hàng trong thời gian",
            tags = {"ADMIN:TOTAL"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "SHOP1000", description = "Tổng số cửa hàng trong thời gian", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Khóa cửa hàng thành công",
                          "data": {
                             "total": "5"
                          },
                          "statusCode": "SHOP1000",
                          "meta": null
                          },
                      }
                    """))}
            ),
    })
    @PostMapping("/count-shop")
    public ResponseEntity<ResponseDto<CountResponse>> countShop(@RequestBody ShopTotalRequest request){
        log.info("countShop() : {}", request);
        return shopService.getTotalShop(request);
    }

    @Operation(
            summary = "Tổng số tài khoản trong thời gian",
            description = "Api Tổng số user trong thời gian",
            tags = {"ADMIN:TOTAL"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "SHOP1000", description = "Lấy tổng số tài khoản trong thời gian thành công", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy tổng số tài khoản trong thời gian thành công",
                          "data": {
                             "total": "5"
                          },
                          "statusCode": "SHOP1000",
                          "meta": null
                          },
                      }
                    """))}
            ),
    })
    @PostMapping("/count-user")
    public ResponseEntity<ResponseDto<CountResponse>> countUser(@RequestBody ShopTotalRequest request){
        log.info("countUser(): {}", request);
        return userService.getTotalUser(request);
    }

    @Operation(
            summary = "Tổng số đánh giá trong thời gian",
            description = "Api Tổng số đánh giá trong thời gian",
            tags = {"ADMIN:TOTAL"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "SHOP1000", description = "Lấy Tổng số đánh giá trong thời gian", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy Tổng số đánh giá trong thời gian",
                          "data": {
                             "total": "5"
                          },
                          "statusCode": "SHOP1000",
                          "meta": null
                          },
                      }
                    """))}
            ),
    })
    @PostMapping("/count-review")
    public ResponseEntity<ResponseDto<CountResponse>> countReview(@RequestBody ShopTotalRequest request){
        log.info("countReview(): {}", request);
        return reviewService.getTotalReview(request);
    }

    @Operation(
            summary = "Lấy tổng số danh mục theo ngày",
            description = "API Lấy tổng số danh mục theo ngày",
            tags = {"ADMIN:TOTAL"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "CATEGORY1000", description = "API Lấy tổng số danh mục theo ngày", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy tổng số danh mục theo ngày thành công",
                          "data": {
                                5
                          },
                          statusCode: "CATEGORY1000"
                      }
                    """))}
            ),
    })
    @PostMapping("/count-category")
    public ResponseEntity<ResponseDto<CountResponse>> getTotalCategory(@RequestBody CategoryCountRequest categoryCountRequest){
        log.info("getTotalCategory: {}", categoryCountRequest);
        return categoryService.countCategory(categoryCountRequest);
    }
}
