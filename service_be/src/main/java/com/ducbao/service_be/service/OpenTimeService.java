package com.ducbao.service_be.service;

import com.ducbao.common.model.builder.ResponseBuilder;
import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.common.model.enums.StatusCodeEnum;
import com.ducbao.service_be.model.dto.response.OpenTimeResponse;
import com.ducbao.service_be.model.entity.OpenTimeModel;
import com.ducbao.service_be.model.mapper.CommonMapper;
import com.ducbao.service_be.repository.OpenTimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenTimeService {
    private final OpenTimeRepository openTimeRepository;
    private final CommonMapper mapper;
    public OpenTimeModel getOpenTimeModel(String id) {
        if(id == null){
            return null;
        }
        return openTimeRepository.findById(id).orElse(null);
    }

    public ResponseEntity<ResponseDto<OpenTimeResponse>> getDetailById(String id) {
        OpenTimeModel openTimeModel = getOpenTimeModel(id);
        if(openTimeModel == null){
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy thời gian",
                    StatusCodeEnum.SHOP1004
            );
        }

        return ResponseBuilder.okResponse(
                "Lấy chi tiết thời gian thành công",
                mapper.map(openTimeModel, OpenTimeResponse.class),
                StatusCodeEnum.SHOP1000
        );
    }
}
