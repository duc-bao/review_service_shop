package com.ducbao.service_be.controller;

import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.service_be.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Tải ảnh avatar user lên hệ thống",
            description = "Api tải ảnh avatar user lên hệ thống",
            tags = {"users"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "IMAGE1201", description = "Tải ảnh avatar lên thành công", content = {@Content(examples = @ExampleObject(value = """
                    {
                    	"success": true,
                    	"message": "Tải ảnh avatar lên thành công",
                    	"data": "http://res.cloudinary.com/dbk09oy6h/image/upload/v1728836118/IMAGE_USER/6704f957a77f0442b1e32a23/1728836117285.jpg.jpg",
                    	"statusCode": "USER1004"
                    }
                    """))
            }),

    })
    @PutMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<String>> uploadFile(@RequestParam("file") MultipartFile file) {
        return userService.uploadAvatar(file);
    }
}
