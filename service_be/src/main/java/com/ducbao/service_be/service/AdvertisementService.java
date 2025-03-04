package com.ducbao.service_be.service;

import com.ducbao.common.model.builder.ResponseBuilder;
import com.ducbao.common.model.dto.MetaData;
import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.common.model.enums.StatusAdvertisement;
import com.ducbao.common.model.enums.StatusCodeEnum;
import com.ducbao.service_be.model.dto.request.AdvertisementRequest;
import com.ducbao.service_be.model.dto.request.PanigationAdvertisementRequest;
import com.ducbao.service_be.model.dto.request.PanigationRequest;
import com.ducbao.service_be.model.dto.response.AdvertisementResponse;
import com.ducbao.service_be.model.entity.AdvertisementModel;
import com.ducbao.service_be.model.mapper.CommonMapper;
import com.ducbao.service_be.repository.AdvertisementRepository;
import com.ducbao.service_be.repository.ShopSearchRepository;
import com.ducbao.service_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final ShopSearchRepository shopSearchRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CommonMapper mapper;
    private final MongoTemplate mongoTemplate;

    public ResponseEntity<ResponseDto<AdvertisementResponse>> createAdvertisement(AdvertisementRequest advertisementRequest) {
        if (advertisementRepository.existsByName(advertisementRequest.getName())) {
            return ResponseBuilder.okResponse(
                    "Đã tồn tại tên gói quảng cáo",
                    StatusCodeEnum.ADVERTISEMENT0101
            );
        }

        AdvertisementModel advertisementModel = mapper.map(advertisementRequest, AdvertisementModel.class);
        advertisementModel.setStatusAdvertisement(StatusAdvertisement.OPEN);
        try {
            advertisementModel = advertisementRepository.save(advertisementModel);
            return ResponseBuilder.okResponse(
                    "Tạo gói quảng cáo thành công",
                    mapper.map(advertisementModel, AdvertisementResponse.class),
                    StatusCodeEnum.ADVERTISEMENT1000
            );
        } catch (Exception e) {
            log.error("Error create advertisement()", e.getMessage());
            return ResponseBuilder.badRequestResponse(
                    "Xảy ra lỗi khi tạo gói quảng cáo",
                    StatusCodeEnum.ADVERTISEMENT0404
            );
        }
    }

    public ResponseEntity<ResponseDto<AdvertisementResponse>> updateAdvertisement(AdvertisementRequest advertisementRequest, String id) {
        AdvertisementModel advertisementModel = advertisementRepository.findById(id).orElse(null);
        if (advertisementModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tồn tại gói quảng cáo",
                    StatusCodeEnum.ADVERTISEMENT0404
            );
        }
        mapper.maptoObject(advertisementRequest, AdvertisementModel.class);
        try {
            advertisementModel = advertisementRepository.save(advertisementModel);
            return ResponseBuilder.okResponse(
                    "Tạo gói quảng cáo thành công",
                    mapper.map(advertisementModel, AdvertisementResponse.class),
                    StatusCodeEnum.ADVERTISEMENT1000
            );
        } catch (Exception e) {
            log.error("Error create advertisement()", e.getMessage());
            return ResponseBuilder.badRequestResponse(
                    "Xảy ra lỗi khi tạo gói quảng cáo",
                    StatusCodeEnum.ADVERTISEMENT0404
            );
        }
    }

    public ResponseEntity<ResponseDto<AdvertisementResponse>> deleteAdvertisement(String id) {
        AdvertisementModel advertisementModel = advertisementRepository.findById(id).orElse(null);
        if (advertisementModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tồn tại gói quảng cáo",
                    StatusCodeEnum.ADVERTISEMENT0404
            );
        }
        advertisementModel.setStatusAdvertisement(StatusAdvertisement.CLOSE);
        try {
            advertisementModel = advertisementRepository.save(advertisementModel);
            return ResponseBuilder.okResponse(
                    "Tạo gói quảng cáo thành công",
                    mapper.map(advertisementModel, AdvertisementResponse.class),
                    StatusCodeEnum.ADVERTISEMENT1000
            );
        } catch (Exception e) {
            log.error("Error create advertisement()", e.getMessage());
            return ResponseBuilder.badRequestResponse(
                    "Xảy ra lỗi khi tạo gói quảng cáo",
                    StatusCodeEnum.ADVERTISEMENT0404
            );
        }
    }

    public ResponseEntity<ResponseDto<List<AdvertisementResponse>>> getListAdvertisement(PanigationAdvertisementRequest request) {
        Sort sort = Optional.ofNullable(request.getSort())
                .map(sortField -> Sort.by(sortField.startsWith("-") ? Sort.Direction.DESC : Sort.Direction.ASC, sortField.replace("-", "")))
                .orElse(Sort.by(Sort.Direction.ASC, "createAt"));

        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit(), sort);

        // Xây dựng truy vấn Criteria
        Criteria criteria = new Criteria();

        Optional.ofNullable(request.getKeyword())
                .filter(keyword -> !keyword.isEmpty())
                .ifPresent(keyword -> criteria.and("name").regex(".*" + keyword + ".*", "i"));

        Optional.ofNullable(request.getStatus())
                .map(StatusAdvertisement::valueOf)
                .ifPresent(status -> criteria.and("statusAdvertisement").is(status.toString()));
        Query query = new Query(criteria).with(pageable);
        List<AdvertisementModel> advertisements = mongoTemplate.find(query, AdvertisementModel.class);
        long count = mongoTemplate.count(query, AdvertisementModel.class);
        List<AdvertisementResponse> advertisementResponses = advertisements.stream()
                .map(
                        advertisementModel -> mapper.map(advertisementModel, AdvertisementResponse.class)
                ).collect(Collectors.toList());

        MetaData metaData = MetaData.builder()
                .total(count)
                .currentPage(request.getPage())
                .totalPage((int) Math.ceil((double) count / request.getLimit()))
                .pageSize(request.getLimit())
                .build();
        return ResponseBuilder.okResponse(
                "Lấy danh sách gói quảng cáo thành công",
                advertisementResponses,
                metaData,
                StatusCodeEnum.ADVERTISEMENT1000
        );
    }

    public ResponseEntity<ResponseDto<AdvertisementResponse>> getById(String id) {
        AdvertisementModel model = advertisementRepository.findById(id).orElse(null);
        if (model == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy gói quảng cáo",
                    StatusCodeEnum.ADVERTISEMENT0404
            );
        }

        try {
            return ResponseBuilder.okResponse(
                    "Lấy thông tin gói quảng cáo thành công",
                    mapper.map(model, AdvertisementResponse.class),
                    StatusCodeEnum.ADVERTISEMENT1000
            );
        } catch (Exception e) {
            log.error("Error getAdvertisement()", e.getMessage());
            return ResponseBuilder.badRequestResponse(
                    "Xảy ra lỗi khi lấy thông tin gói quảng cáo",
                    StatusCodeEnum.ADVERTISEMENT0101
            );
        }
    }

    public ResponseEntity<ResponseDto<AdvertisementResponse>> activeAdvertisement(String id) {
        AdvertisementModel model = advertisementRepository.findById(id).orElse(null);
        if (model == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy gói quảng cáo",
                    StatusCodeEnum.ADVERTISEMENT0404
            );
        }
        if (model.getStatusAdvertisement() == StatusAdvertisement.OPEN) {
            return ResponseBuilder.okResponse(
                    "Gói quảng cáo này đã được kích hoạt sử dụng",
                    StatusCodeEnum.ADVERTISEMENT1000
            );
        }
        model.setStatusAdvertisement(StatusAdvertisement.OPEN);
        try {
            model = advertisementRepository.save(model);
            return ResponseBuilder.okResponse(
                    "Kích hoạt lại gói quảng cáo thành công",
                    mapper.map(model, AdvertisementResponse.class),
                    StatusCodeEnum.ADVERTISEMENT1000
            );
        } catch (Exception e) {
            log.error("Error activeAdvertisement()", e.getMessage());
            return ResponseBuilder.badRequestResponse(
                    "Lỗi khi kích hoạt gói quảng cáo",
                    StatusCodeEnum.ADVERTISEMENT0101
            );
        }

    }
}
