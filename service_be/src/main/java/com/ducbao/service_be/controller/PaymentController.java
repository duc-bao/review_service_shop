package com.ducbao.service_be.controller;

import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.service_be.model.dto.request.ADSShopRequest;
import com.ducbao.service_be.model.dto.request.PanigationRequest;
import com.ducbao.service_be.model.dto.response.HistoryPaymentResponse;
import com.ducbao.service_be.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/pay")
@Slf4j
@RolesAllowed(value = "OWNER")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(
            summary = "Khởi tạo thanh toán",
            description = "Api khởi tạo thanh toán",
            tags = {"OWNER:ADS"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "ADVERTISEMENT1000", description = "khởi tạo thanh toán", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "khởi tạo thanh toán thành công",
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
    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(@RequestBody @Valid ADSShopRequest request) throws UnsupportedEncodingException {
        log.info("Create payment request - {}", request.toString());
        return paymentService.createPayment(request);
    }

    @Operation(
            summary = "Lấy lịch sử giao dịch",
            description = "Api Lấy lịch sử giao dịch",
            tags = {"OWNER:ADS"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "ADVERTISEMENT1000", description = "Lấy lịch sử giao dịch", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy lịch sử giao dịch",
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
    @PostMapping("/history")
    public ResponseEntity<ResponseDto<List<HistoryPaymentResponse>>> getListHistoryPayement(@RequestBody PanigationRequest request){
        log.info("Get list history payment request - {}", request.toString());
        return paymentService.getVnPayInfo(request);
    }
}
