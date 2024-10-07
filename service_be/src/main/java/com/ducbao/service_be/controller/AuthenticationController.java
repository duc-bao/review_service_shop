package com.ducbao.service_be.controller;

import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.service_be.model.dto.request.LoginRequest;
import com.ducbao.service_be.model.dto.request.ResgisterRequest;
import com.ducbao.service_be.model.dto.response.LoginResponse;
import com.ducbao.service_be.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                                    "statusCode": "LOGIN1000"
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
                                    "statusCode": "LOGIN1001"
                                    }
                                    """))}
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<ResponseDto<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto<Void>> register(@RequestBody ResgisterRequest resgisterRequest) {
        return authenticationService.register(resgisterRequest);
    }
}
