package com.ducbao.service_be.service;

import com.ducbao.common.model.builder.ResponseBuilder;
import com.ducbao.common.model.dto.MetaData;
import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.common.model.enums.StatusCodeEnum;
import com.ducbao.common.util.Util;
import com.ducbao.service_be.model.dto.request.ReviewReactionRequest;
import com.ducbao.service_be.model.dto.request.ReviewRequest;
import com.ducbao.service_be.model.dto.request.ReviewUpdateRequest;
import com.ducbao.service_be.model.dto.request.ShopTotalRequest;
import com.ducbao.service_be.model.dto.response.CountResponse;
import com.ducbao.service_be.model.dto.response.ReviewResponse;
import com.ducbao.service_be.model.dto.response.ReviewUserResponse;
import com.ducbao.service_be.model.dto.response.UserReviewInfo;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public ResponseEntity<ResponseDto<List<ReviewUserResponse>>> getListReviewByIdShop(String idShop, int limit, int page, String sort) {

        ShopModel shopModel = shopRepository.findById(idShop).orElse(null);
        if (shopModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy cửa hàng",
                    StatusCodeEnum.SHOP1003
            );
        }

        Sort sort1 = Sort.by(Sort.Direction.DESC, sort);
        Pageable pageable = PageRequest.of(page - 1, limit, sort1);
        Page<ReviewModel> reviewModelPage = reviewRepository.findByIdShop(idShop, pageable);
        List<ReviewModel> reviewModelList = reviewModelPage.getContent();
        List<ReviewUserResponse> reviewUserResponseList = reviewModelList.stream().map(
                reviewModel -> {
                    ReviewUserResponse reviewUserResponse = mapper.map(reviewModel, ReviewUserResponse.class);
                    reviewUserResponse.setUserReviewInfo(infoUser(reviewModel));
                    return reviewUserResponse;
                }
        ).collect(Collectors.toList());

        MetaData metaData = MetaData.builder()
                .total(reviewModelPage.getTotalElements())
                .totalPage(reviewModelPage.getTotalPages())
                .pageSize(limit)
                .currentPage(page)
                .build();
        return ResponseBuilder.okResponse(
                "Lấy danh sách đánh giá theo cửa hàng thành công",
                reviewUserResponseList,
                metaData,
                StatusCodeEnum.REVIEW1000
        );

    }

    public ResponseEntity<ResponseDto<List<ReviewUserResponse>>> getListReviewByIdService(String idService, int limit, int page, String sort) {

        ServiceModel serviceModel = serviceRepository.findById(idService).orElse(null);
        if (serviceModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy cửa hàng",
                    StatusCodeEnum.SHOP1003
            );
        }

        Sort sort1 = Sort.by(Sort.Direction.DESC, sort);
        Pageable pageable = PageRequest.of(page - 1, limit, sort1);
        Page<ReviewModel> reviewModelPage = reviewRepository.findByIdService(idService, pageable);
        List<ReviewModel> reviewModelList = reviewModelPage.getContent();
        List<ReviewUserResponse> reviewUserResponseList = reviewModelList.stream().map(
                reviewModel -> {
                    ReviewUserResponse reviewUserResponse = mapper.map(reviewModel, ReviewUserResponse.class);
                    reviewUserResponse.setUserReviewInfo(infoUser(reviewModel));
                    return reviewUserResponse;
                }
        ).collect(Collectors.toList());

        MetaData metaData = MetaData.builder()
                .total(reviewModelPage.getTotalElements())
                .totalPage(reviewModelPage.getTotalPages())
                .pageSize(limit)
                .currentPage(page)
                .build();
        return ResponseBuilder.okResponse(
                "Lấy danh sách đánh giá theo dịch vụ thành công",
                reviewUserResponseList,
                metaData,
                StatusCodeEnum.REVIEW1000
        );

    }

    public ResponseEntity<ResponseDto<List<ReviewUserResponse>>> getListReviewByIdUser(String idUser, int limit, int page, String sort) {
        UserModel userModel = userRepository.findById(idUser).orElse(null);
        if (userModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Tài khoản không tồn tại",
                    StatusCodeEnum.USER1002
            );
        }

        Sort sort1 = Sort.by(Sort.Direction.DESC, sort);
        Pageable pageable = PageRequest.of(page - 1, limit, sort1);
        Page<ReviewModel> reviewModelPage = reviewRepository.findByIdUser(idUser, pageable);
        List<ReviewModel> reviewModelList = reviewModelPage.getContent();
        List<ReviewUserResponse> reviewUserResponseList = reviewModelList.stream().map(
                reviewModel -> {
                    ReviewUserResponse reviewUserResponse = mapper.map(reviewModel, ReviewUserResponse.class);
                    reviewUserResponse.setUserReviewInfo(infoUser(reviewModel));
                    return reviewUserResponse;
                }
        ).collect(Collectors.toList());

        MetaData metaData = MetaData.builder()
                .total(reviewModelPage.getTotalElements())
                .totalPage(reviewModelPage.getTotalPages())
                .pageSize(limit)
                .currentPage(page)
                .build();
        return ResponseBuilder.okResponse(
                "Lấy danh sách đánh giá theo tài khoản thành công",
                reviewUserResponseList,
                metaData,
                StatusCodeEnum.REVIEW1000
        );
    }

    public ResponseEntity<ResponseDto<List<ReviewUserResponse>>> getListReviewRecently(int limit, int page, String sort) {
        Sort sort1 = Sort.by(Sort.Direction.DESC, sort);
        Pageable pageable = PageRequest.of(page - 1, limit, sort1);
        Page<ReviewModel> reviewModelPage = reviewRepository.findAll(pageable);
        List<ReviewModel> reviewModelList = reviewModelPage.getContent();
        List<ReviewUserResponse> reviewUserResponseList = reviewModelList.stream()
                .map(reviewModel ->  {
                    ReviewUserResponse reviewUserResponse = mapper.map(reviewModel, ReviewUserResponse.class);
                    reviewUserResponse.setUserReviewInfo(infoUser(reviewModel));
                    return reviewUserResponse;
                }).collect(Collectors.toList());

        MetaData metaData = MetaData.builder()
                .total(reviewModelPage.getTotalElements())
                .totalPage(reviewModelPage.getTotalPages())
                .pageSize(limit)
                .currentPage(page)
                .build();

        return ResponseBuilder.okResponse(
                "Lấy danh sách đánh giá mới nhất thành công",
                reviewUserResponseList,
                metaData,
                StatusCodeEnum.REVIEW1000
        );
    }

    public ResponseEntity<ResponseDto<ReviewUserResponse>> updateReviewLike(String idReview, ReviewReactionRequest reviewReactionRequest){
//        String idUser = userService.userId();
//        UserModel userModel1 = userRepository.findById(idUser).orElse(null);
//        if (userModel1 == null) {
//            return ResponseBuilder.badRequestResponse(
//                    "Tài khoản không tồn tại",
//                    StatusCodeEnum.USER1002
//            );
//        }

        ReviewModel reviewModel = reviewRepository.findById(idReview).orElse(null);
        if (reviewModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Đánh giá không tồn tại",
                    StatusCodeEnum.REVIEW1003
            );
        }

        UserModel userModel = userRepository.findById(reviewModel.getIdUser()).orElse(null);
        updateReactionReview(reviewModel, reviewReactionRequest);
        updateReactionUser(userModel, reviewReactionRequest);
        try {
            UserReviewInfo userReviewInfo = mapper.map(userModel, UserReviewInfo.class);
            ReviewUserResponse reviewUserResponse = mapper.map(reviewModel, ReviewUserResponse.class);
            reviewUserResponse.setUserReviewInfo(userReviewInfo);
            userRepository.save(userModel);
            reviewRepository.save(reviewModel);

            return ResponseBuilder.okResponse(
                    "Cập nhật cảm xúc thành công",
                    reviewUserResponse,
                    StatusCodeEnum.REVIEW1000
            );
        }catch (Exception e){
            return ResponseBuilder.badRequestResponse(
                    "Cập nhật cảm xúc đánh giá thất bại",
                    StatusCodeEnum.REVIEW1001
            );
        }

    }

    public ResponseEntity<ResponseDto<CountResponse>> getTotalReview(ShopTotalRequest request){
        int total = reviewRepository.countByCreatedAtBetween(request.getStartDate(), request.getEndDate());
        return ResponseBuilder.okResponse(
                "Lấy tổng số đánh giá trong khoảng thời gian thành công",
                CountResponse.builder().total(total).build(),
                StatusCodeEnum.REVIEW1000
        );
    }

    private void updateReactionUser(UserModel userModel, ReviewReactionRequest reviewReactionRequest) {
        if(reviewReactionRequest.isRemove()){
            switch (reviewReactionRequest.getType()){
                case LIKE -> {
                    if (userModel.getLike() <= 0) return;
                }
                case HELPFUL -> {
                    if (userModel.getHelpful() <= 0) return;
                }
                case NOTLIKE -> {
                    if (userModel.getNotLike() <= 0) return;
                }
            }
        }
        int change  = reviewReactionRequest.isRemove()  ? -1 : 1;
        switch (reviewReactionRequest.getType()) {
            case LIKE -> userModel.setLike(userModel.getLike() + change);
            case HELPFUL -> userModel.setHelpful(userModel.getHelpful() + change);
            case NOTLIKE -> userModel.setNotLike(userModel.getNotLike() + change);
        }

    }

    private void updateReactionReview(ReviewModel reviewModel, ReviewReactionRequest reviewReactionRequest) {
        if (reviewReactionRequest.isRemove()) {
            switch (reviewReactionRequest.getType()) {
                case LIKE -> {
                    if (reviewModel.getLike() <= 0) return;
                }
                case HELPFUL -> {
                    if (reviewModel.getHelpful() <= 0) return;
                }
                case NOTLIKE -> {
                    if (reviewModel.getNotLike() <= 0) return;
                }
            }
        }

        int change  = reviewReactionRequest.isRemove()  ? -1 : 1;
        switch (reviewReactionRequest.getType()) {
            case LIKE -> reviewModel.setLike(reviewModel.getLike() + change);
            case HELPFUL -> reviewModel.setHelpful(reviewModel.getHelpful() + change);
            case NOTLIKE -> reviewModel.setNotLike(reviewModel.getNotLike() + change);
        }
    }

    private UserReviewInfo infoUser(ReviewModel reviewModel) {
        UserModel userModel = userRepository.findById(reviewModel.getIdUser()).orElse(null);
        return mapper.map(userModel, UserReviewInfo.class);
    }
}
