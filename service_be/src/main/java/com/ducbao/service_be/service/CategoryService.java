package com.ducbao.service_be.service;

import com.ducbao.common.model.builder.ResponseBuilder;
import com.ducbao.common.model.dto.MetaData;
import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.common.model.enums.StatusCodeEnum;
import com.ducbao.common.util.Util;
import com.ducbao.service_be.model.dto.request.*;
import com.ducbao.service_be.model.dto.response.CategoryResponse;
import com.ducbao.service_be.model.dto.response.CountResponse;
import com.ducbao.service_be.model.dto.response.TagResponse;
import com.ducbao.service_be.model.entity.CategoryModel;
import com.ducbao.service_be.model.mapper.CommonMapper;
import com.ducbao.service_be.repository.CategoryRepository;
import com.ducbao.service_be.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.json.JsonObject;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final ShopRepository shopRepository;
    private final CommonMapper mapper;

    public ResponseEntity<ResponseDto<CategoryResponse>> createCategory(CategoryRequest categoryRequest) {
        CategoryModel categoryModel = mapper.map(categoryRequest, CategoryModel.class);
        if (categoryModel.getParentId() != null) {
            CategoryModel parent = categoryRepository.findById(categoryModel.getParentId()).orElse(null);
            if (parent == null) {
                return ResponseBuilder.badRequestResponse(
                        "Không tìm thấy thể loại cha",
                        StatusCodeEnum.CATEGORY1002
                );
            }
            if (!parent.getType().equals(categoryModel.getType())) {
                return ResponseBuilder.badRequestResponse(
                        "Không đúng thể loại cha",
                        StatusCodeEnum.CATEGORY1002
                );
            }
            categoryModel.setParentId(parent.getId());
        }
        try {
            categoryModel = categoryRepository.save(categoryModel);
            return ResponseBuilder.okResponse(
                    "Lưu thể loại thành công",
                    mapper.map(categoryModel, CategoryResponse.class),
                    StatusCodeEnum.CATEGORY1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    "Lỗi khi lưu thể loại",
                    StatusCodeEnum.CATEGORY1001
            );
        }
    }

    public ResponseEntity<ResponseDto<CategoryResponse>> updateCategory(CategoryRequest categoryRequest, String id) {
        CategoryModel categoryModel = categoryRepository.findById(id).orElse(null);
        if (categoryModel.getParentId() != null) {
            CategoryModel parent = categoryRepository.findById(categoryModel.getParentId()).orElse(null);
            if (parent == null) {
                return ResponseBuilder.badRequestResponse(
                        "Không tìm thấy thể loại cha",
                        StatusCodeEnum.CATEGORY1002
                );
            }
            if (!parent.getType().equals(categoryModel.getType())) {
                return ResponseBuilder.badRequestResponse(
                        "Không đúng thể loại cha",
                        StatusCodeEnum.CATEGORY1002
                );
            }
            categoryModel.setParentId(parent.getId());
        }
        mapper.maptoObject(categoryRequest, categoryModel);
        try {
            categoryModel = categoryRepository.save(categoryModel);
            return ResponseBuilder.okResponse(
                    "Lưu thể loại thành công",
                    mapper.map(categoryModel, CategoryResponse.class),
                    StatusCodeEnum.CATEGORY1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    "Lỗi khi lưu thể loại",
                    StatusCodeEnum.CATEGORY1001
            );
        }
    }

    public ResponseEntity<ResponseDto<CategoryResponse>> addTags(CategoryTagsRequest request) {
        CategoryModel categoryModel = categoryRepository.findById(request.getIdCategory()).orElse(null);
        if (categoryModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy thể loại",
                    StatusCodeEnum.CATEGORY1002
            );
        }

        if (categoryModel.getParentId() != null) {
            return ResponseBuilder.badRequestResponse(
                    "Không thể tạo thẻ cho thể loại này",
                    StatusCodeEnum.CATEGORY1001
            );
        }
        Set<String> currentTags = categoryModel.getTags();
        if (currentTags == null) {
            currentTags = new HashSet<>();
        }

        if (request.isDelete()) {
            currentTags.removeAll(request.getTags());
        } else {
            currentTags.addAll(request.getTags());
        }
        categoryModel.setTags(currentTags);
        try {
            categoryModel = categoryRepository.save(categoryModel);
            return ResponseBuilder.okResponse(
                    "Thêm mới thẻ cho category thành công",
                    mapper.map(categoryModel, CategoryResponse.class),
                    StatusCodeEnum.CATEGORY1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    "Không thể thêm mới thẻ cho thể loại",
                    StatusCodeEnum.CATEGORY1001
            );
        }

    }

    public ResponseEntity<ResponseDto<List<String>>> getListTag(String idCategory) {
        CategoryModel categoryModel = categoryRepository.findById(idCategory).orElse(null);
        if (categoryModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy danh mục",
                    StatusCodeEnum.CATEGORY1002
            );
        }

        if (categoryModel.getParentId() != null) {
            return ResponseBuilder.badRequestResponse(
                    "Không thể lấy danh sách tag list",
                    StatusCodeEnum.CATEGORY1002
            );
        }

        List<String> tags = categoryModel.getTags().stream().toList();
        return ResponseBuilder.okResponse(
                "Lấy danh sách tag thành công",
                tags,
                StatusCodeEnum.CATEGORY1000
        );
    }

    public ResponseEntity<ResponseDto<CategoryResponse>> deleteTag(CategoryDeleteTagRequest request) {
        CategoryModel categoryModel = categoryRepository.findById(request.getIdCategory()).orElse(null);
        if (categoryModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy danh mục",
                    StatusCodeEnum.CATEGORY1002
            );
        }

        if (categoryModel.getParentId() != null) {
            return ResponseBuilder.badRequestResponse(
                    "Không thể xóa danh sách tag list",
                    StatusCodeEnum.CATEGORY1002
            );
        }
        Set<String> currentTags = categoryModel.getTags();
        if (currentTags != null && request.getTags() != null & !request.getTags().isEmpty()) {
            boolean anyDelete = currentTags.removeAll(request.getTags());
            if (anyDelete) {
                categoryModel.setTags(currentTags);
            }
        }
        try {
            categoryModel = categoryRepository.save(categoryModel);
            return ResponseBuilder.okResponse(
                    "Xóa các tag thành công",
                    mapper.map(categoryModel, CategoryResponse.class),
                    StatusCodeEnum.CATEGORY1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    "Xóa tag thất bại",
                    StatusCodeEnum.CATEGORY1001
            );
        }
    }


    public ResponseEntity<ResponseDto<CategoryResponse>> getById(String id) {
        CategoryModel categoryModel = categoryRepository.findByIdAndIsDelete(id, false);
        if (categoryModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy thể loại cha",
                    StatusCodeEnum.CATEGORY1002
            );
        }

        return ResponseBuilder.okResponse(
                "Lấy thông tin category thành công",
                mapper.map(categoryModel, CategoryResponse.class),
                StatusCodeEnum.CATEGORY1000
        );
    }

    /**
     * Lấy danh sách thể loại với chỉ thằng thể loại gốc
     */
    public ResponseEntity<ResponseDto<List<CategoryResponse>>> getAll(String s, String q, String filter, int limit, int page) {
        Sort sort = Sort.by(Sort.Direction.ASC, s);
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        if (!Util.isNullOrEmpty(q) && !Util.isNullOrEmpty(filter)) {
            JSONObject jsonObject = new JSONObject(filter);
            Page<CategoryModel> categoryResponses = categoryRepository.findByNameContainingAndTypeAndIsDelete(q, jsonObject.get("type").toString(), false, pageable);
            List<CategoryModel> categoryModels = categoryResponses.getContent();

            List<CategoryResponse> categoryResponseList = categoryModels.stream().map(
                    categoryModel -> mapper.map(categoryModel, CategoryResponse.class)
            ).collect(Collectors.toList());

            MetaData metaData = MetaData.builder()
                    .totalPage(categoryResponses.getTotalPages())
                    .currentPage(page)
                    .total(categoryResponses.getTotalElements())
                    .pageSize(limit)
                    .build();

            return ResponseBuilder.okResponse(
                    "Lấy thành công danh sách thể loại với nội dung tìm kiếm và lọc",
                    categoryResponseList,
                    metaData,
                    StatusCodeEnum.CATEGORY1006
            );
        }

        if (!Util.isNullOrEmpty(q)) {
            Page<CategoryModel> categoryResponses = categoryRepository.findByNameContainingAndIsDelete(q, false, pageable);
            List<CategoryModel> categoryModels = categoryResponses.getContent();

            List<CategoryResponse> categoryResponseList = categoryModels.stream().map(
                    categoryModel -> mapper.map(categoryModel, CategoryResponse.class)
            ).collect(Collectors.toList());

            MetaData metaData = MetaData.builder()
                    .totalPage(categoryResponses.getTotalPages())
                    .currentPage(page)
                    .total(categoryResponses.getTotalElements())
                    .pageSize(limit)
                    .build();

            return ResponseBuilder.okResponse(
                    "Lấy thành công danh sách thể loại với nội dung tìm kiếm",
                    categoryResponseList,
                    metaData,
                    StatusCodeEnum.CATEGORY1004
            );
        }

        if (!Util.isNullOrEmpty(filter)) {
            JSONObject jsonObject = new JSONObject(filter);

            Page<CategoryModel> categoryResponses = categoryRepository.findByTypeAndIsDelete(jsonObject.get("type").toString(), false, pageable);
            List<CategoryModel> categoryModels = categoryResponses.getContent();

            List<CategoryResponse> categoryResponseList = categoryModels.stream().map(
                    categoryModel -> mapper.map(categoryModel, CategoryResponse.class)
            ).collect(Collectors.toList());

            MetaData metaData = MetaData.builder()
                    .totalPage(categoryResponses.getTotalPages())
                    .currentPage(page)
                    .total(categoryResponses.getTotalElements())
                    .pageSize(limit)
                    .build();

            return ResponseBuilder.okResponse(
                    "Lấy thành công danh sách thể loại với lọc",
                    categoryResponseList,
                    metaData,
                    StatusCodeEnum.CATEGORY1005
            );
        }
        Page<CategoryModel> categoryModels = categoryRepository.findAllByIsDelete(false, pageable);
        List<CategoryModel> categoryModels1 = categoryModels.getContent();
        List<CategoryResponse> categoryResponseList = categoryModels1.stream().map(
                categoryModel -> mapper.map(categoryModel, CategoryResponse.class)
        ).collect(Collectors.toList());

        MetaData metaData = MetaData.builder()
                .totalPage(categoryModels.getTotalPages())
                .currentPage(page)
                .total(categoryModels.getTotalElements())
                .pageSize(limit)
                .build();
        return ResponseBuilder.okResponse(
                "Lấy thành công danh sách thể loại",
                categoryResponseList,
                metaData,
                StatusCodeEnum.CATEGORY1003
        );
    }

    public ResponseEntity<ResponseDto<CategoryResponse>> deleteCategory(String id) {
        CategoryModel categoryModel = categoryRepository.findById(id).orElse(null);
        if (categoryModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy thể loại cha",
                    StatusCodeEnum.CATEGORY1002
            );
        }

        categoryModel.setDelete(true);
        try {
            categoryModel = categoryRepository.save(categoryModel);
            return ResponseBuilder.okResponse(
                    "Xóa thể loại thành công",
                    mapper.map(categoryModel, CategoryResponse.class),
                    StatusCodeEnum.CATEGORY1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    "Lưu thể loại thất bại",
                    StatusCodeEnum.CATEGORY1001
            );
        }
    }

    /**
     * Create category for user
     */
    public ResponseEntity<ResponseDto<CategoryResponse>> createCategoryForUser(CategoryForUserRequest categoryRequest) {
        CategoryModel categoryParent = categoryRepository.findById(categoryRequest.getIdParent()).orElse(null);
        if (categoryParent == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tồn tại danh mục cha",
                    StatusCodeEnum.CATEGORY1002
            );
        }
        if (categoryParent.getParentId() != null) {
            return ResponseBuilder.badRequestResponse(
                    "Bạn không thể tạo danh mục với id danh mục con",
                    StatusCodeEnum.CATEGORY1001
            );
        }
        Set<String> tags = categoryParent.getTags();
        Set<String> parentTags = categoryParent.getTags().stream().map(
                String::toLowerCase
        ).collect(Collectors.toSet());

        boolean valid = tags.stream().map(
                String::toLowerCase
        ).allMatch(parentTags::contains);

        if (!valid) {
            return ResponseBuilder.badRequestResponse(
                    "Không thể tạo thể loại với tên như này",
                    StatusCodeEnum.CATEGORY1001
            );
        }
        String nameCategory = String.join(",", categoryRequest.getTags());
        CategoryModel categoryModel = CategoryModel.builder()
                .name(nameCategory)
                .tags(categoryRequest.getTags())
                .parentId(categoryRequest.getIdParent())
                .build();

        try {
            categoryModel = categoryRepository.save(categoryModel);
            return ResponseBuilder.okResponse(
                    "Tạo mới danh mục cho user thành công",
                    mapper.map(categoryModel, CategoryResponse.class),
                    StatusCodeEnum.CATEGORY1000
            );
        } catch (Exception e) {
            log.error("CreateCategoryForUser() - {}", e.getMessage());
            return ResponseBuilder.badRequestResponse(
                    "Không thể tạo mới thể loại",
                    StatusCodeEnum.CATEGORY1001
            );
        }
    }


    public ResponseEntity<ResponseDto<TagResponse>> suggestTagForUser(SuggestTagRequest request) {
        CategoryModel categoryModel = categoryRepository.findById(request.getIdCategory()).orElse(null);
        if (categoryModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy thể loại",
                    StatusCodeEnum.CATEGORY1002
            );
        }

        if (categoryModel.getTags() == null || categoryModel.getTags().isEmpty()) {
            return ResponseBuilder.badRequestResponse(
                    "Không có tags nào trong thể loại vui lòng thử lại",
                    StatusCodeEnum.CATEGORY1002
            );
        }

        String keyword = request.getKeyword().trim().toLowerCase();

        Set<String> tags = categoryModel.getTags()
                .stream().
                filter(tag -> tag.toLowerCase().matches(".*" + keyword + ".*"))
                .limit(8)
                .collect(Collectors.toSet());

        return ResponseBuilder.okResponse(
                "Lấy danh sách các tag gợi ý thành công",
                TagResponse.builder().tags(tags).build(),
                StatusCodeEnum.CATEGORY1000
        );
    }

    public ResponseEntity<ResponseDto<Void>> validateNameCategory(CategoryForUserRequest request) {
        CategoryModel categoryModel = categoryRepository.findById(request.getIdParent()).orElse(null);
        if (categoryModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy thể loại",
                    StatusCodeEnum.CATEGORY1002
            );
        }

        if (categoryModel.getTags() == null || categoryModel.getTags().isEmpty()) {
            return ResponseBuilder.badRequestResponse(
                    "Không có tags nào trong thể loại vui lòng thử lại",
                    StatusCodeEnum.CATEGORY1002
            );
        }

        Set<String> normalizedTags = categoryModel.getTags().stream()
                .map(tag -> tag.trim().toLowerCase())
                .collect(Collectors.toSet());

        Set<String> normalizedParentTags = request.getTags().stream()
                .map(tag -> tag.trim().toLowerCase())
                .collect(Collectors.toSet());
        boolean valid = normalizedTags.containsAll(normalizedParentTags);
        if (!valid) {
            return ResponseBuilder.badRequestResponse(
                    "Tên tag không hợp lệ",
                    StatusCodeEnum.CATEGORY1001
            );
        }
        return ResponseBuilder.okResponse(
                "Tên tag hợp lệ với tag danh mục",
                StatusCodeEnum.CATEGORY1000
        );
    }

    public ResponseEntity<ResponseDto<CountResponse>> countCategory(CategoryCountRequest request) {
        Integer total = categoryRepository.countByCreatedAtBetweenAndIsDeleteIsFalse(request.getStartTime(), request.getEndTime());
        return ResponseBuilder.okResponse(
                "Lấy tổng số lượng danh mục thành công",
                CountResponse.builder().total(total).build(),
                StatusCodeEnum.CATEGORY1000
        );
    }


    public CategoryModel getCategory(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return categoryRepository.findById(id).orElse(null);
    }
}
