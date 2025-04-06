package com.ducbao.service_be.service;

import com.ducbao.common.model.builder.ResponseBuilder;
import com.ducbao.common.model.dto.MetaData;
import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.common.model.enums.StatusAdvertisement;
import com.ducbao.common.model.enums.StatusCodeEnum;
import com.ducbao.service_be.model.dto.request.AdvertisementRequest;
import com.ducbao.service_be.model.dto.request.PanigationAdvertisementRequest;
import com.ducbao.service_be.model.dto.request.PanigationRequest;
import com.ducbao.service_be.model.dto.response.AdsSubcriptionResponse;
import com.ducbao.service_be.model.dto.response.AdvertisementResponse;
import com.ducbao.service_be.model.dto.response.ShopResponse;
import com.ducbao.service_be.model.entity.ADSSubscriptionModel;
import com.ducbao.service_be.model.entity.AdvertisementModel;
import com.ducbao.service_be.model.entity.ShopModel;
import com.ducbao.service_be.model.mapper.CommonMapper;
import com.ducbao.service_be.repository.*;
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
import java.time.temporal.ChronoUnit;
import java.util.*;
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
    private final ShopRepository shopRepository;
    private final ADSSubscriptionRepository adsSubscriptionRepository;

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

    public ResponseEntity<ResponseDto<AdsSubcriptionResponse>> getAdsSubscriptionById(String id) {
        String idUser = userService.userId();
        ShopModel shopModel = shopRepository.findById(idUser).orElse(null);
        if (shopModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy cửa hàng",
                    StatusCodeEnum.SHOP1003
            );
        }

        ADSSubscriptionModel adsSubscriptionModel = adsSubscriptionRepository.findById(id).orElse(null);
        if (adsSubscriptionModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "không tìm thấy đăng ký gói quảng cáo",
                    StatusCodeEnum.ADVERTISEMENT0404
            );
        }

        AdvertisementModel advertisementModel = advertisementRepository.findById(adsSubscriptionModel.getIdAdvertisement()).orElse(null);

        if (advertisementModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "không tìm thấy gói quảng cáo",
                    StatusCodeEnum.ADVERTISEMENT0404
            );
        }
        AdsSubcriptionResponse adsSubcriptionResponse = mapper.map(adsSubscriptionModel, AdsSubcriptionResponse.class);
        adsSubcriptionResponse.setName(advertisementModel.getName());
        adsSubcriptionResponse.setDescription(advertisementModel.getDescription());
        Long remainingDay = ChronoUnit.DAYS.between(LocalDateTime.now(), adsSubscriptionModel.getExpiredAt());
        if (remainingDay > 0) {
            adsSubcriptionResponse.setStatusAds("Đang sử dụng");
            adsSubcriptionResponse.setRemainingDay(remainingDay);
        } else {
            adsSubcriptionResponse.setStatusAds("Hết hạn");
            adsSubcriptionResponse.setRemainingDay(0L);
        }
        return ResponseBuilder.okResponse(
                "Lấy thông tin chi tiết gói cước đăng ký thành công",
                adsSubcriptionResponse,
                StatusCodeEnum.ADVERTISEMENT1000
        );

    }

    public ResponseEntity<ResponseDto<List<AdsSubcriptionResponse>>> getListAdsSubscription() {
        String idUser = userService.userId();
        ShopModel shopModel = shopRepository.findById(idUser).orElse(null);
        if (shopModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy cửa hàng",
                    StatusCodeEnum.SHOP1003
            );
        }

        List<ADSSubscriptionModel> adsSubscriptionModelList = adsSubscriptionRepository.findAllByIdShop(shopModel.getId());
        LocalDateTime now = LocalDateTime.now();
        List<AdsSubcriptionResponse> adsSubcriptionResponses = adsSubscriptionModelList.stream()
                .map(adsSubscriptionModel -> {
                    AdvertisementModel advertisementModel = advertisementRepository
                            .findById(adsSubscriptionModel.getIdAdvertisement())
                            .orElse(null);
                    if (advertisementModel == null) {
                        return null; // Nếu không tìm thấy gói, bỏ qua
                    }

                    AdsSubcriptionResponse adsSubcriptionResponse = mapper.map(advertisementModel, AdsSubcriptionResponse.class);
                    adsSubcriptionResponse.setName(advertisementModel.getName());
                    adsSubcriptionResponse.setDescription(advertisementModel.getDescription());

                    // Kiểm tra thời gian hết hạn
                    LocalDateTime endDate = adsSubscriptionModel.getExpiredAt();
                    if (endDate.isBefore(now)) {
                        adsSubcriptionResponse.setStatusAds("Hết hạn");
                    } else {
                        adsSubcriptionResponse.setStatusAds("Đang sử dụng");
                    }

                    return adsSubcriptionResponse;
                })
                .filter(Objects::nonNull) // Loại bỏ các giá trị null
                .collect(Collectors.toList());

        return ResponseBuilder.okResponse(
                "Lấy thông tin gói quảng cáo sử dụng của Cửa hàng thành công",
                adsSubcriptionResponses,
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

    public ResponseEntity<ResponseDto<List<ShopResponse>>> getShopByAdvertisement() {
        List<ADSSubscriptionModel> adsSubscriptionModelList = adsSubscriptionRepository.findAll()
                .stream().filter(adsSubscriptionModel -> adsSubscriptionModel.getExpiredAt().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());

        final String idUser = userService.userId();
        final String idShop = Optional.ofNullable(idUser)
                .map(user -> shopRepository.findByIdUser(user))
                .map(ShopModel::getId)
                .orElse(null);
        List<ShopModel> shopModelList = adsSubscriptionModelList.stream()
                .map(adsSubscriptionModel -> {
                    ShopModel shopModel = shopRepository.findById(adsSubscriptionModel.getIdShop()).orElse(null);
                    return shopModel;
                }).filter(Objects::nonNull)
                .filter(shopModel ->  !shopModel.getId().equals(idShop))
                .collect(Collectors.toList());
        Collections.shuffle(shopModelList);
        List<ShopModel> shopModelListNew = shopModelList.stream().limit(4).collect(Collectors.toList());
        List<ShopResponse> shopResponseList = shopModelListNew.stream()
                .map(
                        shopModel -> mapper.map(shopModel, ShopResponse.class)
                ).collect(Collectors.toList());
        return ResponseBuilder.okResponse(
                "Lấy danh sách cửa hàng được tài trợ thành công",
                shopResponseList,
                StatusCodeEnum.ADVERTISEMENT1000
        );
    }
}
