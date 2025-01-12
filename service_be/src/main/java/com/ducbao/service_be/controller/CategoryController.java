package com.ducbao.service_be.controller;

import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.service_be.model.dto.request.CategoryForUserRequest;
import com.ducbao.service_be.model.dto.request.SuggestTagRequest;
import com.ducbao.service_be.model.dto.response.CategoryResponse;
import com.ducbao.service_be.model.dto.response.TagResponse;
import com.ducbao.service_be.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;

    @Operation(
            summary = "Lấy danh sách danh mục ",
            description = "Api Lấy danh sách danh mục ",
            tags = {"USERS:CAT"},
            parameters = {
                    @Parameter(name = "q", description = "Ô nhập từ tìm kiếm", required = false,
                            schema = @Schema(type = "string")),
                    @Parameter(name = "filter", description = "Điều kiện lọc cho tìm kiếm \n" +
                            "Ví dụ: {\"type\":\"RESTAURANT\"}",
                            required = false,
                            schema = @Schema(type = "string"
                            ))
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "CATEGORY1004", description = "Lấy danh sách danh mục với từ khóa thành công", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy thành công danh sách thể loại với nội dung tìm kiếm",
                          "data": [
                             {
                                 "id": "6710dd81562f193049ca9929",
                                 "name": "Nhà hàng",
                                 "type": "RESTAURANT",
                                 "parentId": null,
                                 "description": "Nhà Hàng siêu ngon",
                                 "delete": false
                             }
                          ],
                          "statusCode": "CATEGORY1003",
                          "meta": {
                              "total": 1,
                              "totalPage": 1,
                              "currentPage": 1,
                              "pageSize": 12
                          }
                      }
                    """))}
            ),
            @ApiResponse(
                    responseCode = "CATEGORY1005", description = "Lấy danh sách danh mục với lọc thành công", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy danh sách danh mục với lọc thành công",
                          "data": [
                             {
                                 "id": "6710dd81562f193049ca9929",
                                 "name": "Nhà hàng",
                                 "type": "RESTAURANT",
                                 "parentId": null,
                                 "description": "Nhà Hàng siêu ngon",
                                 "delete": false
                             }
                          ],
                          "statusCode": "CATEGORY1003",
                          "meta": {
                              "total": 1,
                              "totalPage": 1,
                              "currentPage": 1,
                              "pageSize": 12
                          }
                      }
                    """))}
            ),
            @ApiResponse(
                    responseCode = "CATEGORY1006", description = "Lấy thành công danh sách thể loại với nội dung tìm kiếm và lọc", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy thành công danh sách thể loại với nội dung tìm kiếm và lọc",
                          "data": [
                             {
                                 "id": "6710dd81562f193049ca9929",
                                 "name": "Nhà hàng",
                                 "type": "RESTAURANT",
                                 "parentId": null,
                                 "description": "Nhà Hàng siêu ngon",
                                 "delete": false
                             }
                          ],
                          "statusCode": "CATEGORY1003",
                          "meta": {
                              "total": 1,
                              "totalPage": 1,
                              "currentPage": 1,
                              "pageSize": 12
                          }
                      }
                    """))}
            ),
    })
    @GetMapping("")
    @PreAuthorize("hasAnyRole('OWNER', 'USER')")
    public ResponseEntity<ResponseDto<List<CategoryResponse>>> getAllCategories(
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "limit", defaultValue = "12") int limit,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "filter", required = false) String filter
    ) {
        return categoryService.getAll(sort, q, filter, limit, page);
    }

    @Operation(
            summary = "Lấy thông tin chi tiết của danh mục",
            description = "Api Lấy thông tin chi tiết của danh mục",
            tags = {"USERS:CAT"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "CATEGORY1000", description = "Lấy thông tin category thành công", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy thông tin category thành công",
                          "data": {
                                  "id": "6710dd81562f193049ca9929",
                                  "name": "Nhà Hàng",
                                  "type": "RESTAURANT",
                                  "parentId": null,
                                  "description": "NHà hàng đỉnh cao",
                                  "delete": false
                          },
                          statusCode: "CATEGORY1000"
                      }
                    """))}
            ),
    })
    @PreAuthorize("hasAnyRole('OWNER', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<CategoryResponse>> getCategoryById(@PathVariable String id) {
        return categoryService.getById(id);
    }

    @Operation(
            summary = "Tạo danh mục con cho user",
            description = "Api Tạo danh mục con cho user",
            tags = {"USERS:CAT"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "CATEGORY1000", description = "Tạo danh mục con cho user", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Tạo mới danh mục cho user thành công",
                          "data": {
                                  "id": "6710dd81562f193049ca9929",
                                  "name": "Nhà Hàng",
                                  "type": "RESTAURANT",
                                  "parentId": null,
                                  "description": "NHà hàng đỉnh cao",
                                  "delete": false
                          },
                          statusCode: "CATEGORY1000"
                      }
                    """))}
            ),
            @ApiResponse(
                    responseCode = "CATEGORY1001", description = "Không thể tạo thể loại với tên Tags như này", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Không thể tạo thể loại với tên như này",
                          "data": {
                          },
                          statusCode: "CATEGORY1001"
                      }
                    """))}
            ),
    })
    @PostMapping("/add-cat")
    public ResponseEntity<ResponseDto<CategoryResponse>> addCategory(
            @RequestBody @Valid CategoryForUserRequest categoryForUserRequest
    ){
        log.info("addCategory: {}", categoryForUserRequest);
        return categoryService.createCategoryForUser(categoryForUserRequest);
    }


    @Operation(
            summary = "Lấy danh sách tags gợi ý từ các từ khóa",
            description = "API lấy danh sách tags gợi ý từ các từ khóa",
            tags = {"USERS:CAT"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "CATEGORY1000",
                    description = "Lấy danh sách tags gợi ý từ các từ khóa",
                    content = {
                            @Content(examples = @ExampleObject(value = """
                                    {
                                       "success": true,
                                      "message": "Tạo mới danh mục cho user thành công",
                                      "data": {
                                             tags: [
                                            "mặn",
                                            "ngọt"
                                            ]
                                      },
                                      statusCode: "CATEGORY1000"
                      }
                                    """))
                    }
            )
    })
    @PostMapping("/suggest-tag")
    public ResponseEntity<ResponseDto<TagResponse>> suggestTagForUser(@RequestBody SuggestTagRequest request){
        log.info("suggestTagForUser: {}", request);
        return categoryService.suggestTagForUser(request);
    }
}
