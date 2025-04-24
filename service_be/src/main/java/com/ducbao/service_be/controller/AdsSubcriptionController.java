package com.ducbao.service_be.controller;

import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.service_be.model.dto.request.VerifyShopRequest;
import com.ducbao.service_be.model.dto.response.ShopAdsResponse;
import com.ducbao.service_be.model.dto.response.ShopResponse;
import com.ducbao.service_be.service.AdvertisementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
@Slf4j
public class AdsSubcriptionController {
    private final AdvertisementService advertisementService;

    @Operation(
            summary = "Lấy danh sách cửa hàng được tài trợ",
            description = "Api Lấy danh sách cửa hàng được tài trợ",
            tags = {"USERS:SHOPS"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "SERVICE1000", description = "Lấy danh sách cửa hàng được tài trợ", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy danh sách cửa hàng được tài trợ thành công",
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
    @GetMapping("/get-shop")
    public ResponseEntity<ResponseDto<List<ShopAdsResponse>>> getShopByAdvertisement() {
        log.info("getShopByAdvertisement");
        return advertisementService.getShopByAdvertisement();
    }
}
