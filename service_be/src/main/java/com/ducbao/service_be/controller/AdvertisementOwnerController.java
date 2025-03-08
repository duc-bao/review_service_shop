package com.ducbao.service_be.controller;

import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.service_be.model.dto.request.PanigationAdvertisementRequest;
import com.ducbao.service_be.model.dto.response.AdvertisementResponse;
import com.ducbao.service_be.service.AdvertisementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RolesAllowed(value = "OWNER")
@RequestMapping("/own/ads")
public class AdvertisementOwnerController {
    private final AdvertisementService advertisementService;

    @Operation(
            summary = "Lấy danh sách gói quảng cáo",
            description = "Api Lấy danh sách gói cước",
            tags = {"OWNER:ADS"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "ADVERTISEMENT1000", description = "Lấy danh sách gói quảng cáo thành công", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy danh sách gói quảng cáo thành công",
                          "data": {
                                  "id": "6710dd81562f193049ca9929",
                                  "name": "Nhà Hàng",
                                  "type": "RESTAURANT",
                                  "parentId": null,
                                  "description": "NHà hàng đỉnh cao",
                                  "delete": false
                          },
                          statusCode: "ADVERTISEMENT1000"
                      }
                    """))}
            ),
    })
    @PostMapping("/list-ads")
    public ResponseEntity<ResponseDto<List<AdvertisementResponse>>> getListAds(PanigationAdvertisementRequest request){
        log.info("getListAds - {}", request.toString());
        return advertisementService.getListAdvertisement(request);
    }

    @Operation(
            summary = "Lấy thông tin gói quảng cáo theo id",
            description = "Api Lấy thông tin gói cước theo id",
            tags = {"OWNER:ADS"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "ADVERTISEMENT1000", description = "Lấy thông tin gói cước theo id", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy thông tin gói cước theo id thành công",
                          "data": {
                                  "id": "6710dd81562f193049ca9929",
                                  "name": "Nhà Hàng",
                                  "type": "RESTAURANT",
                                  "parentId": null,
                                  "description": "NHà hàng đỉnh cao",
                                  "delete": false
                          },
                          statusCode: "ADVERTISEMENT1000"
                      }
                    """))}
            ),
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<AdvertisementResponse>> getById(@PathVariable("id") String id){
        log.info("getById - {}", id);
        return advertisementService.getById(id);
    }
}
