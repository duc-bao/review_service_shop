package com.ducbao.service_be.controller;

import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.service_be.model.dto.request.CategoryCountRequest;
import com.ducbao.service_be.model.dto.request.CategoryDeleteTagRequest;
import com.ducbao.service_be.model.dto.request.CategoryRequest;
import com.ducbao.service_be.model.dto.request.CategoryTagsRequest;
import com.ducbao.service_be.model.dto.response.CategoryResponse;
import com.ducbao.service_be.model.dto.response.CountResponse;
import com.ducbao.service_be.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cms/categories")
@RolesAllowed(value = "ADMIN")
@Slf4j
public class CategoryCMSController {
    private static final Logger log = LoggerFactory.getLogger(CategoryCMSController.class);
    private final CategoryService categoryService;

    @Operation(
            summary = "Lấy thông tin chi tiết của danh mục",
            description = "Api Lấy thông tin chi tiết của danh mục",
            tags = {"ADMIN:CAT"})
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
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<CategoryResponse>> getCategoryById(@PathVariable String id) {
        return categoryService.getById(id);
    }

    @Operation(
            summary = "Tạo mới 1 bản ghi danh mục",
            description = "Api Tạo mới 1 bản ghi danh mục",
            tags = {"ADMIN:CAT"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "CATEGORY1000", description = "Tạo mới 1 bản ghi danh mục", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lưu thể loại thành công",
                          "data": {
                                 "id": "6710dd81562f193049ca9929",
                                  "name": "Nhà Hàng",
                                  "type": "RESTAURANT",
                                  "parentId": null,
                                  "description": "NHà hàng đỉnh cao",
                                  "delete": false
                          },
                          "statusCode": "CATEGORY1000",
                          "meta": null
                      }
                    """))}
            ),
    })
    @PostMapping("")
    public ResponseEntity<ResponseDto<CategoryResponse>> createCategory(@RequestBody @Valid CategoryRequest categoryRequest) {
        return categoryService.createCategory(categoryRequest);
    }

    @Operation(
            summary = "Sửa 1 bản ghi danh mục",
            description = "Sửa 1 bản ghi danh mục",
            tags = {"ADMIN:CAT"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "CATEGORY1000", description = "Sửa 1 bản ghi danh mục", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lưu thể loại thành công",
                          "data": {
                                 "id": "6710dd81562f193049ca9929",
                                 "name": "Nhà hàng",
                                 "type": "RESTAURANT",
                                 "parentId": null,
                                 "description": "Nhà Hàng siêu ngon",
                                 "delete": false
                          },
                          statusCode: "CATEGORY1000"
                      }
                    """))}
            ),
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<CategoryResponse>> updateCategory(@PathVariable String id, @RequestBody CategoryRequest categoryRequest) {
        return categoryService.updateCategory(categoryRequest, id);
    }

    @Operation(
            summary = "Lấy danh sách danh mục ",
            description = "Api Lấy danh sách danh mục ",
            tags = {"ADMIN:CAT"},
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
            summary = "Xóa 1 bản ghi danh mục",
            description = "API xóa 1 bản ghi danh mục",
            tags = {"ADMIN:CAT"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "CATEGORY1000", description = "Xóa 1 bản ghi danh mục", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Xóa 1 bản ghi danh mụ thành công",
                          "data": {
                                 "id": "6710dd81562f193049ca9929",
                                 "name": "Nhà hàng",
                                 "type": "RESTAURANT",
                                 "parentId": null,
                                 "description": "Nhà Hàng siêu ngon",
                                 "delete": true
                          },
                          statusCode: "CATEGORY1000"
                      }
                    """))}
            ),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<CategoryResponse>> deleteCategoryById(@PathVariable String id){
        return categoryService.deleteCategory(id);
    }

    @Operation(
            summary = "Cập nhật tags cho danh mục cha",
            description = "API Cập nhật tags cho danh mục cha",
            tags = {"ADMIN:CAT"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "CATEGORY1000", description = "Cập nhật tags cho danh mục cha", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Thêm mới thẻ cho category thành công",
                          "data": {
                                 "id": "6710dd81562f193049ca9929",
                                 "name": "Nhà hàng",
                                 "type": "RESTAURANT",
                                 "parentId": "6710dd81562f193049ca9",
                                 "description": "Nhà Hàng siêu ngon",
                                 "tags": "Chay, mặn"
                                 "delete": true
                          },
                          statusCode: "CATEGORY1000"
                      }
                    """))}
            ),
    })
    @PutMapping("/add-tags")
    public ResponseEntity<ResponseDto<CategoryResponse>> addTags(@RequestBody @Valid CategoryTagsRequest categoryTagsRequest){
        log.info("addTags: {}", categoryTagsRequest);
        return categoryService.addTags(categoryTagsRequest);
    }

    @Operation(
            summary = "Xóa tag cho danh mục cha",
            description = "API xóa tag cho danh mục cha",
            tags = {"ADMIN:CAT"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "CATEGORY1000", description = "API xóa tag cho danh mục cha", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Xóa tag cho danh mục cha thành công",
                          "data": {
                                 "id": "6710dd81562f193049ca9929",
                                 "name": "Nhà hàng",
                                 "type": "RESTAURANT",
                                 "parentId": "6710dd81562f193049ca9",
                                 "description": "Nhà Hàng siêu ngon",
                                 "tags": "Chay, mặn"
                                 "delete": true
                          },
                          statusCode: "CATEGORY1000"
                      }
                    """))}
            ),
    })
    @PostMapping("/delete-tags")
    public ResponseEntity<ResponseDto<CategoryResponse>> deleteTags(@RequestBody @Valid CategoryDeleteTagRequest categoryTagsRequest){
        log.info("deleteTags: {}", categoryTagsRequest);
        return categoryService.deleteTag(categoryTagsRequest);
    }

    @Operation(
            summary = "Lấy danh sách tag cho danh mục",
            description = "API Lấy danh sách tag cho danh mục",
            tags = {"ADMIN:CAT"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "CATEGORY1000", description = "API Lấy danh sách tag cho danh mục", content = {@Content(examples = @ExampleObject(value = """
                     {
                          "success": true,
                          "message": "Lấy danh sách tag cho danh mục thành công",
                          "data": {
                               "chay, mặn"
                          },
                          statusCode: "CATEGORY1000"
                      }
                    """))}
            ),
    })
    @GetMapping("/list-tags/{id}")
    public ResponseEntity<ResponseDto<List<String>>> getAllTags(@PathVariable String id){
        log.info("getAllTags: {}", id);
        return categoryService.getListTag(id);
    }
}
