package com.ducbao.service_be.service;

import com.ducbao.common.model.builder.ResponseBuilder;
import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.common.model.enums.StatusCodeEnum;
import com.ducbao.service_be.model.dto.response.CountResponse;
import com.ducbao.service_be.model.dto.response.ListAdsSubResponse;
import com.ducbao.service_be.model.dto.response.ListHistoryResponse;
import com.ducbao.service_be.model.dto.response.ListReviewResponse;
import com.ducbao.service_be.model.entity.*;
import com.ducbao.service_be.model.mapper.CommonMapper;
import com.ducbao.service_be.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticAService {
    private final ADSSubscriptionRepository adsSubscriptionRepository;
    private final AdvertisementRepository advertisementRepository;
    private final ShopRepository shopRepository;
    private final HistoryPaymentRepository historyPaymentRepository;
    private final UserService userService;
    private final ReviewRepository reviewRepository;
    private final CommonMapper mapper;

    public ResponseEntity<ResponseDto<CountResponse>> getTotalAdvertisement() {
        List<ADSSubscriptionModel> adsSubscriptionModels = adsSubscriptionRepository.findAll();
        Integer totalAmount = 0;
        int totalAds = 0;
        if (!adsSubscriptionModels.isEmpty()) {
            totalAds = adsSubscriptionModels.size();
            for (ADSSubscriptionModel adsSubscriptionModel : adsSubscriptionModels) {
                String advertisement = adsSubscriptionModel.getIdAdvertisement();
                if (advertisement != null && !advertisement.isEmpty()) {
                    AdvertisementModel advertisementModel = advertisementRepository.findById(advertisement).get();
                    if (advertisementModel != null) {
                        totalAmount += advertisementModel.getPrice();
                    }
                }
            }
        }
        return ResponseBuilder.okResponse(
                "Lấy thông kê doanh thu thành công",
                CountResponse.builder().total(totalAmount).build(),
                StatusCodeEnum.ADVERTISEMENT1000
        );
    }

    /**
     * Lấy doanh thu thành công
     */
    public ResponseEntity<ResponseDto<List<ListHistoryResponse>>> getRevenue() {
        List<HistoryPaymentModel> historyPaymentModels = historyPaymentRepository.findAll();
        List<ListHistoryResponse> historyResponses = historyPaymentModels
                .stream().map(historyPaymentModel -> mapper.map(historyPaymentModel, ListHistoryResponse.class)).collect(Collectors.toList());
        return ResponseBuilder.okResponse(
                "Lấy danh sách thống kê lịch sử thành công",
                historyResponses,
                StatusCodeEnum.SHOP1000
        );
    }

    /**
     * Lấy thông kê danh sách gói quảng cáo đnáh ký
     */
    public ResponseEntity<ResponseDto<List<ListAdsSubResponse>>> getListAdsSubscription() {
        List<ADSSubscriptionModel> adsSubscriptionModels = adsSubscriptionRepository.findAll();
        List<ListAdsSubResponse> listAdsSubResponses = adsSubscriptionModels.stream().map(
                adsSubscriptionModel -> mapper.map(adsSubscriptionModel, ListAdsSubResponse.class)
        ).collect(Collectors.toList());
        return ResponseBuilder.okResponse(
                "Lấy danh sách đăng ký gói cước thành công",
                listAdsSubResponses,
                StatusCodeEnum.ADVERTISEMENT1000
        );
    }

    public ResponseEntity<ResponseDto<List<ListReviewResponse>>> getListReviewModel1() {
        String idUser = userService.userId();
        ShopModel shopModel = shopRepository.findByIdUser(idUser);
        if (shopModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy cửa hàng",
                    StatusCodeEnum.SHOP1004
            );
        }

        List<ReviewModel> reviewModels = reviewRepository.findAllByIdShop(shopModel.getId());
        List<ListReviewResponse> responses = reviewModels.stream()
                .map(reviewModel -> mapper.map(reviewModel, ListReviewResponse.class)).collect(Collectors.toList());
        return ResponseBuilder.okResponse(
                "Lấy danh sách đánh giá thành công",
                responses,
                StatusCodeEnum.REVIEW1000
        );
    }
}
