package com.ducbao.service_be.controller;

import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.service_be.model.dto.request.PanigationRequest;
import com.ducbao.service_be.model.dto.response.UserResponse;
import com.ducbao.service_be.service.UserService;
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
@RequestMapping("/cms/users")
@RequiredArgsConstructor
@Slf4j
@RolesAllowed(value = "ADMIN")
public class UserCMSController {
    private final UserService userService;

    @Operation(
            summary = "Lấy danh sách thông tin tài khoản",
            description = "Api Lấy danh sách thông tin tài khoản",
            tags = {"ADMIN:USER"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "CATEGORY1000", description = "Lấy danh sách thông tin tài khoản thành công", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy danh sách thông tin tài khoản thành công",
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
                          statusCode: "CATEGORY1000"
                      }
                    """))}
            ),
    })
    @PostMapping("/list-user")
    public ResponseEntity<ResponseDto<List<UserResponse>>> getListUser(@RequestBody @Valid PanigationRequest panigationRequest){
        return userService.getListUser(panigationRequest);
    }

    @Operation(
            summary = "Khóa tài khoản",
            description = "Api Khóa tài khoản",
            tags = {"ADMIN:USER"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "CATEGORY1000", description = "Khóa tài khoản thành công", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Khóa tài khoản thành công",
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
                          statusCode: "CATEGORY1000"
                      }
                    """))}
            ),
    })
    @PutMapping("/block-user/{id}")
    public ResponseEntity<ResponseDto<UserResponse>> blockUser(@PathVariable(value = "id") String idUser){
        return userService.blockAccount(idUser);
    }
}
