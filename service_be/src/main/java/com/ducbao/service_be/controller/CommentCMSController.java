package com.ducbao.service_be.controller;

import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.service_be.model.dto.request.CommentRequest;
import com.ducbao.service_be.model.dto.response.CommentResponse;
import com.ducbao.service_be.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/owner/comments")
@RequiredArgsConstructor
@Slf4j
@RolesAllowed({"OWNER"})
public class CommentCMSController {
    private final CommentService commentService;

    @Operation(
            summary = "Lấy comment theo id review thành công",
            description = "Api Lấy comment theo id review thành công",
            tags = {"OWNER:COMMENTS"}
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
        return commentService.getByIdReview(idReview);
    }

    @Operation(
            summary = "Tạo comment với chủ cửa hàng",
            description = "Api Tạo comment với chủ cửa hàng",
            tags = {"OWNER:COMMENTS"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "COMMENT1000", description = "Tạo comment với chủ cửa hàng", content = {@Content(examples = @ExampleObject(value = """
                     {
                    	"success": true,
                    	"message": "Tạo comment thành công",
                    	"data": "id": "6721f1211de8b954494d7c55",
                                 "content": "chủ shopp comment lại nhé",
                                 "idReview": "671a6d635adc7b74155a9349",
                                 "idUser": null,
                                 "idShop": "6718c19c43333b7137e625f8",
                                  "comment": true,
                    	"statusCode": "COMMENT1000"
                    }
                    """))}
            ),
            @ApiResponse(responseCode = "COMMENT1001", description = "Tồn tại comment của đánh giá này rồi", content = {@Content(examples = @ExampleObject(value = """
                     {
                    	"success": false,
                    	"message": "Bạn không được phép tạo comment cho đánh giá này nữa",
                    	"data": {},
                    	"statusCode": "COMMENT1001"
                    }
                    """))}
            ),
            @ApiResponse(responseCode = "COMMENT1001", description = "Tài khoản này không được phép tạo bình luận", content = {@Content(examples = @ExampleObject(value = """
                     {
                    	"success": false,
                    	"message": "Tài khoản này không được phép tạo bình luận",
                    	"data": {},
                    	"statusCode": "COMMENT1001"
                    }
                    """))}
            ),
    })
    @PostMapping("/{idReview}")
    public ResponseEntity<ResponseDto<CommentResponse>> addComment(@PathVariable("idReview") String idReview, @RequestBody CommentRequest commentRequest) {
        return commentService.createComment(commentRequest, idReview);
    }

    @Operation(
            summary = "Cập nhật comment theo id comment",
            description = "Api Cập nhật comment theo id comment",
            tags = {"OWNER:COMMENTS"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "COMMENT1000", description = "Cập nhật comment theo id comment", content = {@Content(examples = @ExampleObject(value = """
                     {
                    	"success": true,
                    	"message": "Cập nhật comment thành công",
                    	"data": "id": "6721f1211de8b954494d7c55",
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
    @PutMapping("/{idComment}")
    public ResponseEntity<ResponseDto<CommentResponse>> updateComment(@PathVariable("idComment") String idComment, @RequestBody CommentRequest commentRequest) {
        return commentService.updateComment(idComment, commentRequest);
    }

    @Operation(
            summary = "Xóa comment ra khỏi đánh giá",
            description = "Api Xóa comment ra khỏi đánh giá",
            tags = {"OWNER:COMMENTS"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "COMMENT1000", description = "Xóa comment ra khỏi đánh giá", content = {@Content(examples = @ExampleObject(value = """
                     {
                    	"success": true,
                    	"message": "Xóa commnet thành công",
                    	"data": 
                    	"statusCode": "COMMENT1000"
                    }
                    """))}
            ),
    })
    @DeleteMapping("/{idComment}")
    public ResponseEntity<ResponseDto<Void>> deleteComment(@PathVariable("idComment") String idComment) {
        return commentService.deleteComment(idComment);
    }
}
