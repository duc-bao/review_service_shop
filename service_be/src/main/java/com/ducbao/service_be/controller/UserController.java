package com.ducbao.service_be.controller;

import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.service_be.model.dto.request.UserChangePassword;
import com.ducbao.service_be.model.dto.request.UserForgotPassword;
import com.ducbao.service_be.model.dto.request.UserRequest;
import com.ducbao.service_be.model.dto.response.UserResponse;
import com.ducbao.service_be.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
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
            summary = "Tải ảnh đại diện avatar user lên hệ thống",
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

    @Operation(
            summary = "Cập nhật tài khoản thành công",
            description = "Api Cập nhật tài khoản thành công ",
            tags = {"users"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "SHOP1000", description = "Cập nhật tài khoản thành công", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Cập nhật tài khoản thành công",
                          "data": {
                               "id": "6704f957a77f0442b1e32a23",
                               "username": "ducbao",
                               "email": "anhbao200222@britizhschool.edu.pl",
                               "phone": "0203032671",
                               "city": "asssccccs",
                               "avatar": "https://example.com/avatar.jpg",
                               "ward": "Ward 3",
                               "district": "District 1",
                               "firstName": "Dca",
                               "lastName": "Baccc",
                               "dateOfBirth": null
                          },
                          "statusCode": "USER1005",
                          "meta": null
                      }
                    """))}
            ),
    })
    @PutMapping("/change-profile")
    public ResponseEntity<ResponseDto<UserResponse>> changeProfile(@RequestBody UserRequest userRequest) {
        return userService.changeProfile(userRequest);
    }

    @SecurityRequirements( value = {})
    @Operation(
            summary = "Gửi mật khẩu tạm thời cho việc quên mật khẩu ",
            description = "Api Gửi mật khẩu tạm thời cho việc quên mật khẩu",
            tags = {"users"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "USER1000", description = "Đã gửi mật khẩu tạm thời thành công", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Đã gửi mật khẩu tạm thời thành công",
                          "data": {
                               "id": "6704f957a77f0442b1e32a23",
                               "username": "ducbao",
                               "email": "anhbao200222@britizhschool.edu.pl",
                               "phone": "0203032671",
                               "city": "asssccccs",
                               "avatar": "https://example.com/avatar.jpg",
                               "ward": "Ward 3",
                               "district": "District 1",
                               "firstName": "Dca",
                               "lastName": "Baccc",
                               "dateOfBirth": null
                          },
                          "statusCode": "USER1000",
                          "meta": null
                      }
                    """))}
            ),
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseDto<UserResponse>> forgotPassword(@RequestBody UserForgotPassword userForgotPassword) {
        return userService.forgotPassword(userForgotPassword);
    }

    @Operation(
            summary = "Thay đổi mật khẩu",
            description = "Api Thay đổi mật khẩu",
            tags = {"users"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "USER1001", description = "Đổi mật khẩu thành công", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Đổi mật khẩu thành công",
                          "data": {
                               "id": "6704f957a77f0442b1e32a23",
                               "username": "ducbao",
                               "email": "anhbao200222@britizhschool.edu.pl",
                               "phone": "0203032671",
                               "city": "asssccccs",
                               "avatar": "https://example.com/avatar.jpg",
                               "ward": "Ward 3",
                               "district": "District 1",
                               "firstName": "Dca",
                               "lastName": "Baccc",
                               "dateOfBirth": null
                          },
                          "statusCode": "USER1001",
                          "meta": null
                      }
                    """))}
            ),
    })
    @PutMapping("/change-password")
    public ResponseEntity<ResponseDto<UserResponse>> changePassword(@RequestBody UserChangePassword userChangePassword) {
        return userService.changePassword(userChangePassword);
    }

    @Operation(
            summary = "Lấy thông tin user theo Id",
            description = "Api Lấy thông tin user theo Id",
            tags = {"users"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "USER1006", description = "Lấy thông tin tài khoản theo id thành công", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy thông tin tài khoản theo id thành công",
                          "data": {
                               "id": "6704f957a77f0442b1e32a23",
                               "username": "ducbao",
                               "email": "anhbao200222@britizhschool.edu.pl",
                               "phone": "0203032671",
                               "city": "asssccccs",
                               "avatar": "https://example.com/avatar.jpg",
                               "ward": "Ward 3",
                               "district": "District 1",
                               "firstName": "Dca",
                               "lastName": "Baccc",
                               "dateOfBirth": null
                          },
                          "statusCode": "USER1006",
                          "meta": null
                      }
                    """))}
            ),
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<UserResponse>> getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

}
