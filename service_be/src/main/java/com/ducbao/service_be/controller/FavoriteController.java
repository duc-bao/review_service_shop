package com.ducbao.service_be.controller;

import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.service_be.model.dto.request.FavoriteRequest;
import com.ducbao.service_be.model.dto.response.FavoriteResponse;
import com.ducbao.service_be.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
@Slf4j
public class FavoriteController {
    private final FavoriteService favoriteService;

    @Operation(
            summary = "Lấy danh sách yêu thích cửa hàng",
            description = "Api Lấy danh sách yêu thích cửa hàng ",
            tags = {"USERS:FAVORITES"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "FAVORITE1000", description = "Lấy danh sách yêu thích cửa hàng ", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy danh sách yêu thích cửa hàng thành công",
                          "data": [
                                    {
                                      "id": "67431b877fea4177d7e8af51",
                                      "idUser": "67225ca1cd4c221182e91f2f",
                                      "idShop": "67224d42cd4c221182e91f22"
                                    },
                                    {
                                      "id": "67431b347fea4177d7e8af4e",
                                      "idUser": "67225ca1cd4c221182e91f2f",
                                      "idShop": "67224ca4cd4c221182e91f1a"
                                    }
                                  ],
                          statusCode: "FAVORITE1000"
                      }
                    """))}
            ),

    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<ResponseDto<List<FavoriteResponse>>> getListFavorite(
            @RequestParam(name = "sort", defaultValue = "createdAt") String s,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "12") int limit
    ){
        return favoriteService.getListFavorite(s, limit, page);
    }

    @Operation(
            summary = "Lấy cửa hàng yêu thích theo id",
            description = "Api Lấy cửa hàng yêu thích theo id ",
            tags = {"USERS:FAVORITES"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "FAVORITE1000", description = "Lấy  cửa hàng yêu thích theo id", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy cửa hàng yêu thích theo id thành công",
                         "data": {
                                "id": "67431b877fea4177d7e8af51",
                                "idUser": "67225ca1cd4c221182e91f2f",
                                "idShop": "67224d42cd4c221182e91f22"
                              },
                          statusCode: "FAVORITE1000"
                      }
                    """))}
            ),

    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<FavoriteResponse>> getFavoriteById(@PathVariable(value = "id") String id){
        return favoriteService.getFavoriteById(id);
    }

    @Operation(
            summary = "Thêm cửa hàng yêu thích",
            description = "Api Thêm cửa hàng yêu thích",
            tags = {"USERS:FAVORITES"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "FAVORITE1000", description = "Thêm cửa hàng yêu thích ", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Thêm cửa hàng yêu thích thành công",
                          "data": {
                                "id": "67431b877fea4177d7e8af51",
                                "idUser": "67225ca1cd4c221182e91f2f",
                                "idShop": "67224d42cd4c221182e91f22"
                          },
                          statusCode: "FAVORITE1000"
                      }
                    """))}
            ),
            @ApiResponse(
                    responseCode = "FAVORITE1000", description = "Đã tồn tại yêu thích cửa hàng này", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Đã tồn tại yêu thích cửa hàng này",
                          "data": {
                                "id": "67431b877fea4177d7e8af51",
                                "idUser": "67225ca1cd4c221182e91f2f",
                                "idShop": "67224d42cd4c221182e91f22"
                              },
                          statusCode: "FAVORITE1000"
                      }
                    """))}
            ),


    })
    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseDto<FavoriteResponse>> addFavorite(@RequestBody FavoriteRequest favoriteRequest){
        return favoriteService.addFavorite(favoriteRequest);
    }

    @Operation(
            summary = "Xóa cửa hàng yêu thích thành công",
            description = "Api Xóa cửa hàng yêu thích thành công",
            tags = {"USERS:FAVORITES"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "FAVORITE1000", description = "Xóa cửa hàng yêu thích", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Xóa cửa hàng yêu thích thành công",
                          "data": {
                                "id": "67431b877fea4177d7e8af51",
                                "idUser": "67225ca1cd4c221182e91f2f",
                                "idShop": "67224d42cd4c221182e91f22"
                          },
                          statusCode: "FAVORITE1000"
                      }
                    """))}
            ),

    })
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseDto<FavoriteResponse>> deleteFavorite(@PathVariable(value = "id") String id){
        return favoriteService.deleteFavorite(id);
    }
}
