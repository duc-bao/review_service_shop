package com.ducbao.service_be.controller;

import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.service_be.model.dto.request.*;
import com.ducbao.service_be.model.dto.response.LoginResponse;
import com.ducbao.service_be.model.dto.response.UserInfoResponse;
import com.ducbao.service_be.service.AuthenticationService;
import com.ducbao.service_be.service.GoogleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
@SecurityRequirements(value = {})
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final GoogleService googleService;

    @Operation(
            summary = "Đăng nhập với tài khoản và mật khẩu",
            description = "Api đăng nhập tài khoản và mật khẩu",
            tags = {"Auth"}
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
                                        accessToken:"eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjpbIlVTRVIiXSwiZW5hYmxlZCI6dHJ1ZSwic3ViIjoiNjcxYTVlZjc2YjdjMWQ0ODY5OGM2ZDcxIiwiZXhwIjoxNzMwOTkxMTMyfQ.nhv9qzZWC5OLebisxRCf33LhXj4xApwcvoRzhj7RHDAv5eU_J15mIqzs0qCOA1HtKiJ0o8szflwuGc3vZHEuqQ"
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
            summary = "Đăng kí tài khoản với user",
            description = "Api đăng kí tài khoản với user",
            tags = {"Auth"}
    )
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "USER1000",
                            description = "Đăng kí tài khoản thành công với user",
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
            summary = "Đăng kí tài khoản là chủ cửa hàng",
            description = "Đăng kí tài khoản với chủ cửa hàng",
            tags = {"Auth"}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "SHOP1000",
                            description = "Đăng kí tài khoản là chủ cửa hàng thành công",
                            content = {
                                    @Content(examples = @ExampleObject(value = """
                                             {
                                            "success": true,
                                            "message": "Đăng kí tài khoản thành công vui lòng kiểm tra email để kích hoạt tài khoản",
                                            "data": null,
                                            "statusCode": "USER1000",
                                            "meta": null
                                            }
                                            """))
                    }
                    ),
                    @ApiResponse(
                            responseCode = "LOGIN1002",
                            description = "Email đã tồn tài",
                            content = {
                                    @Content(examples = @ExampleObject(value = """
                                             {
                                            "success": false,
                                            "message": "Email đã tồn tại",
                                            "data": null,
                                            "statusCode": "LOGIN1002",
                                            "meta": null
                                            }
                                            """))
                            }
                    )
            }
    )
    @PostMapping("/register/shop")
    public ResponseEntity<ResponseDto<Void>> registerShop(@RequestBody RegisterShopOwner resgisterRequest) {
        return authenticationService.registerWithShop(resgisterRequest);
    }
    @Operation(
            summary = "Kích hoạt tài khoản",
            description = "Api kích hoạt tài khoản",
            tags = {"Auth"}
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

    @Operation(
            summary = "Kiểm tra xem email tồn tại hay chưa",
            description = "Api Kiểm tra xem email tồn tại hay chưa",
            tags = {"Auth"}
    )
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "USER1007",
                            description = "Kiểm tra xem email tồn tại hay chưa",
                            content = {@Content(examples = @ExampleObject(value = """
                                    {
                                    "success":true,
                                    "message":"Đã tồn tại email",
                                    "data":{
                                        true
                                    },
                                    "statusCode":"USER1007",
                                    "meta":null}
                                     """))}
                    ),
                    @ApiResponse(
                            responseCode = "USER1000",
                            description = "Kiểm tra xem email tồn tại hay chưa",
                            content = {@Content(examples = @ExampleObject(value = """
                                    {
                                    "success":false,
                                    "message":"Email chưa tồn tại"
                                    "data": false,
                                    "statusCode": "USER1000",
                                    "meta": null
                                    }
                                    """))}
                    )
            }
    )
    @GetMapping("/exists-email")
    public ResponseEntity<ResponseDto<Boolean>> existsEmail(@RequestParam String email) {
        return authenticationService.existEmail(email);
    }

    @Operation(
            summary = "Kiểm tra xem username tồn tại hay chưa",
            description = "Api Kiểm tra xem username tồn tại hay chưa",
            tags = {"Auth"}
    )
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "USER1007",
                            description = "Kiểm tra xem username tồn tại hay chưa",
                            content = {@Content(examples = @ExampleObject(value = """
                                    {
                                    "success":true,
                                    "message":"Đã tồn tại username",
                                    "data":{
                                        true
                                    },
                                    "statusCode":"USER1007",
                                    "meta":null}
                                     """))}
                    ),
                    @ApiResponse(
                            responseCode = "USER1000",
                            description = "Kiểm tra xem username tồn tại hay chưa",
                            content = {@Content(examples = @ExampleObject(value = """
                                    {
                                    "success":false,
                                    "message":"username chưa tồn tại"
                                    "data": false,
                                    "statusCode": "USER1000",
                                    "meta": null
                                    }
                                    """))}
                    )
            }
    )
    @GetMapping("/exists-username")
    public ResponseEntity<ResponseDto<Boolean>> existsUserName(@RequestParam String username) {
        return authenticationService.exitsUsername(username);
    }

    @Operation(
            summary = "Đăng xuất tài khoản",
            description = "Api đăng xuất tài khoản",
            tags = {"Auth"}
    )
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "USER1007",
                            description = "Đăng xuất tài khoản thành công",
                            content = {@Content(examples = @ExampleObject(value = """
                                    {
                                    "success":true,
                                    "message":"Đăng xuất tài khoản thành công",
                                    "data":{
                                        null
                                    },
                                    "statusCode":"USER1000",
                                    "meta":null}
                                     """))}
                    )
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<ResponseDto<Void>> logout(@RequestBody LogoutRequest request){
        log.info("logout - {}", request);
        return authenticationService.logout(request);
    }

    @Operation(
            summary = "Đăng nhập với google thành công",
            description = "Api đăng nhập với google thành công",
            tags = {"Auth"}
    )
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "LOGIN1000",
                            description = "Đăng nhập với google thành công",
                            content = {@Content(examples = @ExampleObject(value = """
                                    {
                                    "success":true,
                                    "message":"Đăng nhập với google thành công"
                                    "data":{
                                        accessToken:"eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjpbIlVTRVIiXSwiZW5hYmxlZCI6dHJ1ZSwic3ViIjoiNjcxYTVlZjc2YjdjMWQ0ODY5OGM2ZDcxIiwiZXhwIjoxNzMwOTkxMTMyfQ.nhv9qzZWC5OLebisxRCf33LhXj4xApwcvoRzhj7RHDAv5eU_J15mIqzs0qCOA1HtKiJ0o8szflwuGc3vZHEuqQ"
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
    @PostMapping("/login-google")
    public ResponseEntity<ResponseDto<LoginResponse>> loginGoogle(@RequestBody GoogleRequest request){
        log.info("login-google - {}", request);
        return googleService.loginGoogle(request);
    }
}
