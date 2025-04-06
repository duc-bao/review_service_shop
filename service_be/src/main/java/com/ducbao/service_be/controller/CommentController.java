package com.ducbao.service_be.controller;

import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.service_be.model.dto.response.CommentResponse;
import com.ducbao.service_be.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @Operation(
            summary = "Lấy comment theo id review thành công",
            description = "Api Lấy comment theo id review thành công",
            tags = {"USER:COMMENTS"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "COMMENT1000", description = "Lấy comment theo id review thành công", content = {@Content(examples = @ExampleObject(value = """
                     {
                    	"success": true,
                    	"message": "Lấy comment thành công theo id review",
                    	"data":"id": "6721f1211de8b954494d7c55",
                                "content": "chủ shopp comment lại nhé",
                                "idReview": "671a6d635adc7b74155a9349",
                                "idUser": null,
                                "idShop": "6718c19c43333b7137e625f8",
                                "comment": true,
                    	"statusCode": "COMMENT1000"
                    }
                    """))}
            ),
    })
    @GetMapping("/{idReview}")
    public ResponseEntity<ResponseDto<CommentResponse>> getByIdReview(@PathVariable("idReview") String idReview) {
        log.info("Get comment by idReview - {}", idReview);
        return commentService.getCommentByIdReview(idReview);
    }
}
