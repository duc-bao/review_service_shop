package com.ducbao.service_be.service;

import com.ducbao.common.model.builder.ResponseBuilder;
import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.common.model.enums.StatusCodeEnum;
import com.ducbao.service_be.model.dto.request.CommentRequest;
import com.ducbao.service_be.model.dto.response.CommentResponse;
import com.ducbao.service_be.model.entity.CommentModel;
import com.ducbao.service_be.model.entity.ReviewModel;
import com.ducbao.service_be.model.entity.ShopModel;
import com.ducbao.service_be.model.entity.UserModel;
import com.ducbao.service_be.model.mapper.CommonMapper;
import com.ducbao.service_be.repository.CommentRepository;
import com.ducbao.service_be.repository.ReviewRepository;
import com.ducbao.service_be.repository.ShopRepository;
import com.ducbao.service_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final CommonMapper mapper;

    public ResponseEntity<ResponseDto<CommentResponse>> createComment(CommentRequest commentRequest, String idReview) {
        String idUser = userService.userId();
        UserModel   userModel = userRepository.findById(idUser).orElse(null);
        if(userModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Tài khoản không tồn tại",
                    StatusCodeEnum.USER1002
            );
        }

        ShopModel shopModel = shopRepository.findByIdUser(idUser);
        if(shopModel == null){
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy cửa hàng",
                    StatusCodeEnum.SHOP1003
            );
        }

        ReviewModel reviewModel = reviewRepository.findById(idReview).orElse(null);
        if(reviewModel == null){
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy review",
                    StatusCodeEnum.REVIEW1003
            );
        }

        if(!reviewModel.getIdShop().equals(shopModel.getId())){
            return ResponseBuilder.badRequestResponse(
                    "Tài khoản này không được phép tạo bình luận",
                    StatusCodeEnum.SHOP1003
            );
        }

        CommentModel commentModel1 = commentRepository.findByIdReview(idReview);
        if(commentModel1 != null){
            return ResponseBuilder.badRequestResponse(
                    "Bạn không được phép tạo comment cho đánh giá này nữa",
                    StatusCodeEnum.COMMENT1001
            );
        }

        CommentModel commentModel = mapper.map(commentRequest, CommentModel.class);
        commentModel.setIdReview(idReview);
        commentModel.setIdShop(shopModel.getId());
        commentModel.setComment(true);
        try {
            commentModel = commentRepository.save(commentModel);
            return ResponseBuilder.okResponse(
                    "Tạo comment thành công",
                    mapper.map(commentModel, CommentResponse.class),
                    StatusCodeEnum.COMMENT1000
            );
        }catch (Exception e){
            return ResponseBuilder.badRequestResponse(
                    "Lưu comment thất bại",
                    StatusCodeEnum.COMMENT1001
            );
        }
    }

    public ResponseEntity<ResponseDto<CommentResponse>> updateComment(String idComment, CommentRequest  commentRequest) {
        CommentModel commentModel = commentRepository.findById(idComment).orElse(null);
        if(commentModel == null){
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy comment",
                    StatusCodeEnum.COMMENT1002
            );
        }
        boolean isVery = isVeryfyShop(commentModel.getIdReview()).getBody().isSuccess();
        if(!isVery){
            return ResponseBuilder.badRequestResponse(
                    "Không được phép tạo bình luận",
                    StatusCodeEnum.COMMENT1001
            );
        }


        mapper.maptoObject(commentRequest, commentModel);
        try {
            commentModel = commentRepository.save(commentModel);
            return ResponseBuilder.okResponse(
                    "Cập nhật comment thành công",
                    mapper.map(commentModel, CommentResponse.class),
                    StatusCodeEnum.COMMENT1000
            );
        }catch (Exception e){
            return ResponseBuilder.badRequestResponse(
                    "Lưu comment thất bại",
                    StatusCodeEnum.COMMENT1001
            );
        }

    }

    public ResponseEntity<ResponseDto<CommentResponse>> getByIdReview(String idReview) {
        CommentModel commentModel = commentRepository.findByIdReview(idReview);
        if(commentModel == null){
            return ResponseBuilder.badRequestResponse(
                    "Không tồn tại bình luận",
                    StatusCodeEnum.COMMENT1002
            );
        }
        boolean isVery = isVeryfyShop(commentModel.getIdReview()).getBody().isSuccess();
        if(!isVery){
            return ResponseBuilder.badRequestResponse(
                    "Không được lấy bình luận",
                    StatusCodeEnum.COMMENT1001
            );
        }


        return ResponseBuilder.okResponse(
                "Lấy comment thành công theo id review",
                mapper.map(commentModel, CommentResponse.class),
                StatusCodeEnum.COMMENT1000
        );
    }

    public ResponseEntity<ResponseDto<Void>> deleteComment(String idComment) {
        CommentModel commentModel = commentRepository.findById(idComment).orElse(null);
        if(commentModel == null){
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy comment",
                    StatusCodeEnum.COMMENT1002
            );
        }
        boolean isVery = isVeryfyShop(commentModel.getIdReview()).getBody().isSuccess();
        if(!isVery){
            return ResponseBuilder.badRequestResponse(
                    "Không được xóa bình luận",
                    StatusCodeEnum.COMMENT1001
            );
        }
        try {
            commentRepository.delete(commentModel);
            return ResponseBuilder.okResponse(
                    "Xóa commnet thành công",
                    StatusCodeEnum.COMMENT1000
            );
        }catch (Exception e){
            return ResponseBuilder.badRequestResponse(
                    "Xóa comment thất bại",
                    StatusCodeEnum.COMMENT1001
            );
        }
    }

    private ResponseEntity<ResponseDto<Boolean>> isVeryfyShop(String idReview){
        String idUser = userService.userId();
        UserModel   userModel = userRepository.findById(idUser).orElse(null);
        if(userModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Tài khoản không tồn tại",
                    StatusCodeEnum.USER1002
            );
        }

        ShopModel shopModel = shopRepository.findByIdUser(idUser);
        if(shopModel == null){
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy cửa hàng",
                    StatusCodeEnum.SHOP1003
            );
        }

        ReviewModel reviewModel = reviewRepository.findById(idReview).orElse(null);
        if(!reviewModel.getIdShop().equals(shopModel.getId())){
            return ResponseBuilder.badRequestResponse(
                    "Tài khoản này không được phép tạo bình luận",
                    StatusCodeEnum.SHOP1003
            );
        }
        return ResponseBuilder.okResponse(
                "Đã xác thực đúng",
                true,
                StatusCodeEnum.COMMENT
        );
    }
}
