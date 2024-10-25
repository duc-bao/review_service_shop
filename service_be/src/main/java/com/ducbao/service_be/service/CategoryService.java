package com.ducbao.service_be.service;

import com.ducbao.common.model.builder.ResponseBuilder;
import com.ducbao.common.model.dto.MetaData;
import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.common.model.enums.StatusCodeEnum;
import com.ducbao.common.util.Util;
import com.ducbao.service_be.model.dto.request.CategoryRequest;
import com.ducbao.service_be.model.dto.response.CategoryResponse;
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

import java.util.List;
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
        if(categoryModel.getParentId() != null){
            CategoryModel parent = categoryRepository.findById(categoryModel.getParentId()).orElse(null);
            if(parent == null){
                return ResponseBuilder.badRequestResponse(
                        "Không tìm thấy thể loại cha",
                        StatusCodeEnum.CATEGORY1002
                );
            }
            if(!parent.getType().equals(categoryModel.getType())){
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
        }catch (Exception e){
            return ResponseBuilder.badRequestResponse(
                    "Lỗi khi lưu thể loại",
                    StatusCodeEnum.CATEGORY1001
            );
        }
    }

    public ResponseEntity<ResponseDto<CategoryResponse>> updateCategory(CategoryRequest categoryRequest, String id) {
        CategoryModel categoryModel = categoryRepository.findById(id).orElse(null);
        if(categoryModel.getParentId() != null){
            CategoryModel parent = categoryRepository.findById(categoryModel.getParentId()).orElse(null);
            if(parent == null){
                return ResponseBuilder.badRequestResponse(
                        "Không tìm thấy thể loại cha",
                        StatusCodeEnum.CATEGORY1002
                );
            }
            if(!parent.getType().equals(categoryModel.getType())){
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
        }catch (Exception e){
            return ResponseBuilder.badRequestResponse(
                    "Lỗi khi lưu thể loại",
                    StatusCodeEnum.CATEGORY1001
            );
        }
    }

    public ResponseEntity<ResponseDto<CategoryResponse>> getById(String id) {
        CategoryModel categoryModel = categoryRepository.findByIdAndIsDelete(id, false);
        if(categoryModel == null){
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
     *
    * */
    public ResponseEntity<ResponseDto<List<CategoryResponse>>> getAll(String s, String q, String filter, int limit, int page) {
        Sort sort = Sort.by(Sort.Direction.ASC, s);
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        if(!Util.isNullOrEmpty(q) && !Util.isNullOrEmpty(filter)){
            JSONObject jsonObject = new JSONObject(filter);
            Page<CategoryModel> categoryResponses = categoryRepository.findByNameContainingAndTypeAndIsDelete(q,jsonObject.get("type").toString(), false,pageable);
            List<CategoryModel>  categoryModels = categoryResponses.getContent();

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

        if(!Util.isNullOrEmpty(q)){
            Page<CategoryModel> categoryResponses = categoryRepository.findByNameContainingAndIsDelete(q,false ,pageable);
            List<CategoryModel>  categoryModels = categoryResponses.getContent();

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

        if(!Util.isNullOrEmpty(filter)){
            JSONObject jsonObject = new JSONObject(filter);

            Page<CategoryModel> categoryResponses = categoryRepository.findByTypeAndIsDelete(jsonObject.get("type").toString(), false,pageable);
            List<CategoryModel>  categoryModels = categoryResponses.getContent();

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
        Page<CategoryModel> categoryModels = categoryRepository.findAllByIsDelete(false,pageable);
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

    public ResponseEntity<ResponseDto<CategoryResponse>> deleteCategory(String id){
        CategoryModel categoryModel = categoryRepository.findById(id).orElse(null);
        if(categoryModel == null){
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
        }catch (Exception e){
            return ResponseBuilder.badRequestResponse(
                    "Lưu thể loại thất bại",
                    StatusCodeEnum.CATEGORY1001
            );
        }
    }

}
