package com.ducbao.service_be.service;

import com.ducbao.common.model.builder.ResponseBuilder;
import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.common.model.enums.StatusCodeEnum;
import com.ducbao.common.util.Util;
import com.ducbao.service_be.model.dto.request.ReviewRequest;
import com.ducbao.service_be.model.dto.request.ReviewUpdateRequest;
import com.ducbao.service_be.model.dto.response.ReviewResponse;
import com.ducbao.service_be.model.entity.ReviewModel;
import com.ducbao.service_be.model.entity.ServiceModel;
import com.ducbao.service_be.model.entity.ShopModel;
import com.ducbao.service_be.model.entity.UserModel;
import com.ducbao.service_be.model.mapper.CommonMapper;
import com.ducbao.service_be.repository.ReviewRepository;
import com.ducbao.service_be.repository.ServiceRepository;
import com.ducbao.service_be.repository.ShopRepository;
import com.ducbao.service_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final ShopRepository shopRepository;
    private final CommonMapper mapper;
    private final RedissonClient redisson;
    private final UserService userService;

    public ResponseEntity<ResponseDto<ReviewResponse>> createReview(ReviewRequest reviewRequest) {
        String idUser = userService.userId();
        UserModel userModel = userRepository.findById(idUser).orElse(null);
        if (userModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Tài khoản không tồn tại",
                    StatusCodeEnum.USER1002
            );
        }

        ShopModel existsShop = shopRepository.findByIdUser(userModel.getId());
        if (existsShop != null) {
            return ResponseBuilder.badRequestResponse(
                    "Cửa hàng không được đi đánh giá các review khác",
                    StatusCodeEnum.REVIEW1002
            );
        }

        if (!Util.isNullOrEmpty(reviewRequest.getIdShop()) && reviewRequest.getIdService() == null) {
            ShopModel shopModel = shopRepository.findById(reviewRequest.getIdShop()).orElse(null);
            if (shopModel == null) {
                return ResponseBuilder.badRequestResponse(
                        "Cửa hàng không tồn tại",
                        StatusCodeEnum.SHOP1003
                );
            }

            ReviewModel reviewModel = mapper.map(reviewRequest, ReviewModel.class);
            shopModel.setCountReview(shopModel.getCountReview() + 1);
            reviewModel.setLike(0);
            reviewModel.setHelpful(0);
            reviewModel.setNotLike(0);
            reviewModel.setIdUser(userModel.getId());
            userModel.setRatingUser(userModel.getRatingUser() + 1);
            int quantityImage = reviewRequest.getMediaUrlReview().size();
            userModel.setQuantityImage(userModel.getQuantityImage() + quantityImage);

            try {
                reviewModel = reviewRepository.save(reviewModel);
                shopRepository.save(shopModel);
                userRepository.save(userModel);
                return ResponseBuilder.okResponse(
                        "Tạo đánh giá của cửa hàng thành công",
                        mapper.map(reviewModel, ReviewResponse.class),
                        StatusCodeEnum.REVIEW1000
                );
            } catch (Exception e) {
                return ResponseBuilder.badRequestResponse(
                        "Tạo review thất bại có lỗi xảy ra",
                        StatusCodeEnum.REVIEW1001
                );
            }
        }

        ServiceModel serviceModel = serviceRepository.findById(reviewRequest.getIdService()).orElse(null);
        if (serviceModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Dịch vụ không tồn tại vui lòng thử lại",
                    StatusCodeEnum.SERVICE1003
            );
        }

        ShopModel shopModel = shopRepository.findById(serviceModel.getIdShop()).orElse(null);

        serviceModel.setCountReview(serviceModel.getCountReview() + 1);
        shopModel.setCountReview(shopModel.getCountReview() + 1);
        ReviewModel reviewModel = mapper.map(reviewRequest, ReviewModel.class);
        reviewModel.setLike(0);
        reviewModel.setHelpful(0);
        reviewModel.setNotLike(0);
        reviewModel.setIdUser(userModel.getId());
        reviewModel.setIdShop(shopModel.getId());
        userModel.setRatingUser(userModel.getRatingUser() + 1);
        int quantityImage = reviewRequest.getMediaUrlReview().size();
        userModel.setQuantityImage(userModel.getQuantityImage() + quantityImage);

        try {
            reviewModel = reviewRepository.save(reviewModel);
            shopRepository.save(shopModel);
            userRepository.save(userModel);
            serviceRepository.save(serviceModel);
            return ResponseBuilder.okResponse(
                    "Tạo đánh giá của dịch vụ thành công",
                    mapper.map(reviewModel, ReviewResponse.class),
                    StatusCodeEnum.REVIEW1000
            );
        }catch (Exception e){
            return ResponseBuilder.badRequestResponse(
                    "Tạo review thất bại có lỗi xảy ra",
                    StatusCodeEnum.REVIEW1001
            );
        }
    }

    public ResponseEntity<ResponseDto<ReviewResponse>> updateReview(ReviewUpdateRequest reviewRequest, String id) {
        String idUser = userService.userId();

        UserModel userModel = userRepository.findById(idUser).orElse(null);
        if (userModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy tài khoản vui lòng đăng nhập",
                    StatusCodeEnum.USER1002
            );
        }

        ReviewModel reviewModel = reviewRepository.findById(id).orElse(null);
        if (reviewModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Đánh giá không tồn tại",
                    StatusCodeEnum.REVIEW1003
            );
        }

        if(!reviewModel.getIdUser().equals(userModel.getId()) || reviewModel.isEdit()){
            return ResponseBuilder.badRequestResponse(
                    "Bạn không thể sửa đổi đánh giá này",
                    StatusCodeEnum.USER1001
            );
        }
        mapper.maptoObject(reviewRequest, reviewModel);
        reviewModel.setEdit(true);

        try {
            reviewModel = reviewRepository.save(reviewModel);
            return ResponseBuilder.okResponse(
                    "Cập nhật đánh giá thành công",
                    mapper.map(reviewModel, ReviewResponse.class),
                    StatusCodeEnum.REVIEW1000
            );
        }catch (Exception e){
            return ResponseBuilder.badRequestResponse(
                    "Cập nhật đánh giá thất bại",
                    StatusCodeEnum.REVIEW1001
            );
        }

    }

    public ResponseEntity<ResponseDto<Void>> deleteReview(String id) {
        String idUser = userService.userId();
        ReviewModel reviewModel = reviewRepository.findById(id).orElse(null);
        if (reviewModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Đánh giá không tồn tại",
                    StatusCodeEnum.REVIEW1003
            );
        }
        UserModel userModel = userRepository.findById(idUser).orElse(null);
        if (userModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Tài khoản không tồn tại",
                    StatusCodeEnum.USER1002
            );
        }

        if(reviewModel.getIdShop() != null && reviewModel.getIdService() == null){
            ShopModel shopModel = shopRepository.findById(reviewModel.getIdShop()).orElse(null);
            if(shopModel == null){
                return ResponseBuilder.badRequestResponse(
                        "Cửa hàng không tồn tại",
                        StatusCodeEnum.SHOP1003
                );
            }
            shopModel.setCountReview(shopModel.getCountReview() - 1);
            userModel.setRatingUser(userModel.getRatingUser() - 1);
            userModel.setHelpful(userModel.getHelpful() - reviewModel.getHelpful());
            userModel.setLike(userModel.getLike()  - reviewModel.getLike());
            userModel.setNotLike(userModel.getNotLike() - reviewModel.getNotLike());
            if (reviewModel.getMediaUrlReview() != null) {
                userModel.setQuantityImage(userModel.getQuantityImage() - reviewModel.getMediaUrlReview().size());
            }
            try {
                shopRepository.save(shopModel);
                userRepository.save(userModel);
                reviewRepository.delete(reviewModel);
                return ResponseBuilder.okResponse(
                        "Xóa đánh giá thành công",
                        StatusCodeEnum.REVIEW1004
                );
            }catch (Exception e){
                return  ResponseBuilder.badRequestResponse(
                        "Xóa đánh giá thất bại",
                        StatusCodeEnum.REVIEW1001
                );
            }
        }

        ServiceModel serviceModel = serviceRepository.findById(reviewModel.getIdService()).orElse(null);
        if(serviceModel == null){
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy dịch vụ",
                    StatusCodeEnum.SERVICE1003
            );
        }
        serviceModel.setCountReview(serviceModel.getCountReview() - 1);
        ShopModel shopModel = shopRepository.findById(serviceModel.getIdShop()).orElse(null);
        shopModel.setCountReview(shopModel.getCountReview() - 1);
        userModel.setRatingUser(userModel.getRatingUser() - 1);
        userModel.setHelpful(userModel.getHelpful() - reviewModel.getHelpful());
        userModel.setLike(userModel.getLike()  - reviewModel.getLike());
        userModel.setNotLike(userModel.getNotLike() - reviewModel.getNotLike());
        if (reviewModel.getMediaUrlReview() != null) {
            userModel.setQuantityImage(userModel.getQuantityImage() - reviewModel.getMediaUrlReview().size());
        }
        try {
            serviceRepository.save(serviceModel);
            shopRepository.save(shopModel);
            userRepository.save(userModel);
            reviewRepository.delete(reviewModel);
            return ResponseBuilder.okResponse(
                    "Xóa đánh giá thành công",
                    StatusCodeEnum.REVIEW1004
            );
        }catch (Exception e){
            return ResponseBuilder.badRequestResponse(
                    "Xóa đánh giá thất bại",
                    StatusCodeEnum.REVIEW1001
            );
        }
    }
}
