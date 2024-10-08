package com.ducbao.service_be.controller;

import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.service_be.model.dto.request.LoginRequest;
import com.ducbao.service_be.model.dto.request.ResgisterRequest;
import com.ducbao.service_be.model.dto.response.LoginResponse;
import com.ducbao.service_be.model.dto.response.UserInfoResponse;
import com.ducbao.service_be.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Operation(
            summary = "Đăng nhập với tài khoản và mật khẩu",
            description = "Api đăng nhập tài khoản và mật khẩu",
            tags = {"auth"}
    )
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "LOGIN1000",
                            description = "Đăng nhập thành công",
                            content = {@Content(examples = @ExampleObject(value = """
                                    {
                                    "success":true,
                                    "message":"Đăng nhập thành công"
                                    "data":{
                                        accessToken:"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwMDA3MDEyMzU4NCIsImlhdCI6MTcxNTMxNDIwNSwiZXhwIjoxNzE2NTIzODA1fQ.HemoUsztf_OGgM-KEeDIU1388MkX4n7S1hzpeY49eXCAwFL_8IF0KenQpk5mqsKsb0RZVDXb_cru3fuyuET0Xg"
                                        userInfoResponse:{
                                            username: "anhbao",
                                            email:"truongducbao@gmail.com",
                                            avatar:"acscscc",
                                            statusUserEnums:ACTIVE,
                                        }        
                                    },
                                    "statusCode": "LOGIN1000",
                                    "meta": null
                                    }
                                    """))}
                    ),
                    @ApiResponse(
                            responseCode = "LOGIN1001",
                            description = "Đăng nhập không thành công",
                            content = {@Content(examples = @ExampleObject(value = """
                                    {
                                    "success":true,
                                    "message":"Tên đăng nhập hoặc mật khẩu không chính xác"
                                    "data":{
                                        null 
                                    },
                                    "statusCode": "LOGIN1001",
                                    "meta": null
                                    }
                                    """))}
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<ResponseDto<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @Operation(
            summary = "Đăng kí tài khoản",
            description = "Api đăng kí tài khoản",
            tags = {"auth"}
    )
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "USER1000",
                            description = "Đăng kí tài khoản thành công",
                            content = {@Content(examples = @ExampleObject(value = """
                                    {
                                    "success": true,
                                    "message": "Đăng kí tài khoản thành công vui lòng kiểm tra email để kích hoạt tài khoản",
                                    "data": null,
                                    "statusCode": "USER1000",
                                    "meta": null
                                    }
                                    """))}
                    ),
                    @ApiResponse(
                            responseCode = "USER1001",
                            description = "Đăng kí không thành công",
                            content = {@Content(examples = @ExampleObject(value = """
                                    {
                                    "success":false,
                                    "message":"Lưu người dùng không thành công"
                                    "data": null,
                                    "statusCode": "USER1001",
                                    "meta": null
                                    }
                                    """))}
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<ResponseDto<Void>> register(@RequestBody ResgisterRequest resgisterRequest) {
        return authenticationService.register(resgisterRequest);
    }

    @Operation(
            summary = "Kích hoạt tài khoản",
            description = "Api kích hoạt tài khoản",
            tags = {"auth"}
    )
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "USER1000",
                            description = "Đăng kí tài khoản thành công",
                            content = {@Content(examples = @ExampleObject(value = """
                                    {
                                    "success":false,
                                    "message":"Kích hoạt tài khoản thành công",
                                    "data":{
                                        "username":"ducbao",
                                        "email":"anhbao200222@britizhschool.edu.pl",
                                        "avatar":null,
                                        "statusUserEnums":"ACTIVE"
                                    },
                                    "statusCode":"USER1003",
                                    "meta":null}
                                     """))}
                    ),
                    @ApiResponse(
                            responseCode = "USER1001",
                            description = "Đăng kí không thành công",
                            content = {@Content(examples = @ExampleObject(value = """
                                    {
                                    "success":false,
                                    "message":"Lưu người dùng không thành công"
                                    "data": null,
                                    "statusCode": "USER1001",
                                    "meta": null
                                    }
                                    """))}
                    )
            }
    )
    @GetMapping("/active-account")
    public ResponseEntity<ResponseDto<UserInfoResponse>> activeAccount(@RequestParam String code) {
        log.info("Đã đi vào đây");
        return authenticationService.activeAcount(code);
    }
}
