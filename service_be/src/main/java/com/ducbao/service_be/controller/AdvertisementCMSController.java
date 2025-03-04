package com.ducbao.service_be.controller;

import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.service_be.model.dto.request.AdvertisementRequest;
import com.ducbao.service_be.model.dto.request.PanigationAdvertisementRequest;
import com.ducbao.service_be.model.dto.response.AdvertisementResponse;
import com.ducbao.service_be.model.dto.response.CategoryResponse;
import com.ducbao.service_be.service.AdvertisementService;
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
@RequiredArgsConstructor
@Slf4j
@RolesAllowed(value = "ADMIN")
public class AdvertisementCMSController {
    private final AdvertisementService advertisementService;

    @Operation(
            summary = "Tạo gói quảng cáo",
            description = "Api Tạo gói quảng cáo",
            tags = {"ADMIN:ADS"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "ADVERTISEMENT1000", description = "Tạo gói quảng cáo thành công", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Tạo gói quảng cáo thành công",
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
    @PostMapping("/create-advertisement")
    public ResponseEntity<ResponseDto<AdvertisementResponse>> createAdvertisement(@RequestBody @Valid AdvertisementRequest request) {
        log.info("Create advertisement {}", request.toString());
        return advertisementService.createAdvertisement(request);
    }

    @Operation(
            summary = "Lấy thông tin gói quảng cáo theo id",
            description = "Api Lấy thông tin gói cước theo id",
            tags = {"ADMIN:ADS"})
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
    public ResponseEntity<ResponseDto<AdvertisementResponse>> getById(@PathVariable("id") String id) {
        log.info("Get by id {}");
        return advertisementService.getById(id);
    }

    @Operation(
            summary = "Lấy danh sách gói quảng cáo",
            description = "Api Lấy danh sách gói cước",
            tags = {"ADMIN:ADS"})
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
    @PostMapping("")
    public ResponseEntity<ResponseDto<List<AdvertisementResponse>>> getListAdvertisement(@RequestBody @Valid PanigationAdvertisementRequest request) {
        log.info("Get list advertisement() - {}", request.toString());
        return advertisementService.getListAdvertisement(request);
    }

    @Operation(
            summary = "Kích hoạt gói quảng cáo",
            description = "Api kích hoạt gói quảng cáo",
            tags = {"ADMIN:ADS"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "ADVERTISEMENT1000", description = "Lấy Kích hoạt gói quảng cáo thành công", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": " Kích hoạt gói quảng cáo thành công",
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
    @PutMapping("/active-ads/{id}")
    public ResponseEntity<ResponseDto<AdvertisementResponse>> activeAdvertisement(@PathVariable("id") String id) {
        log.info("Active Advertisement {}", id);
        return advertisementService.activeAdvertisement(id);
    }

    @Operation(
            summary = "Tạm dừng gói quảng cáo",
            description = "Api tạm dừng gói quảng cáo",
            tags = {"ADMIN:ADS"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "ADVERTISEMENT1000", description = "Lấy tạm dừng gói quảng cáo thành công", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Tạm dừng gói quảng cáo thành công",
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
    @PutMapping("/deactive-ads/{id}")
    public ResponseEntity<ResponseDto<AdvertisementResponse>> deactiveAds(@PathVariable("id") String id) {
        log.info("Deactive Advertisement {}", id);
        return advertisementService.deleteAdvertisement(id);
    }

    @Operation(
            summary = "Cập nhật gói quảng cáo",
            description = "Api Cập nhật gói quảng cáo",
            tags = {"ADMIN:ADS"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "ADVERTISEMENT1000", description = "Lấy Cập nhật gói quảng cáoo thành công", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": " Cập nhật gói quảng cáo thành công",
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
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<AdvertisementResponse>> updateAdvertisement(@RequestBody @Valid AdvertisementRequest request,@PathVariable("id") String id) {
        log.info("Update Advertisement {}", request);
        return advertisementService.updateAdvertisement(request, id);
    }
}
