package com.ducbao.service_be.service;

import com.ducbao.common.model.builder.ResponseBuilder;
import com.ducbao.common.model.constant.FileConstant;
import com.ducbao.common.model.dto.MetaData;
import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.common.model.enums.StatusCodeEnum;
import com.ducbao.common.model.enums.StatusUserEnums;
import com.ducbao.service_be.model.constant.AppConstants;
import com.ducbao.service_be.model.dto.request.*;
import com.ducbao.service_be.model.dto.response.CountResponse;
import com.ducbao.service_be.model.dto.response.UserResponse;
import com.ducbao.service_be.model.entity.ServiceModel;
import com.ducbao.service_be.model.entity.UserModel;
import com.ducbao.service_be.model.mapper.CommonMapper;
import com.ducbao.service_be.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final FileService fileService;
    private final CommonMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final MongoTemplate mongoTemplate;

    public ResponseEntity<ResponseDto<String>> uploadAvatar(MultipartFile file) {
        String userId = userId();
        String url = fileService.upload(file, userId, FileConstant.IMAGE_USER);
        return ResponseBuilder.okResponse(
                "Tải ảnh lên thành công",
                url,
                StatusCodeEnum.USER1004
        );
    }

    public ResponseEntity<ResponseDto<UserResponse>> changeProfile(UserRequest userRequest) {
        String idUser = userId();
        UserModel userModel = userRepository.findById(idUser).orElse(null);
        if (userModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Tài khoản không tồn tại",
                    StatusCodeEnum.USER1002
            );
        }

        mapper.maptoObject(userRequest, userModel);
        try {
            userRepository.save(userModel);
            return ResponseBuilder.okResponse(
                    "Cập nhật tài khoản thành công",
                    mapper.map(userModel, UserResponse.class),
                    StatusCodeEnum.USER1005
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    "Lưu tài khoản thất bại",
                    StatusCodeEnum.USER1001
            );
        }
    }

    public ResponseEntity<ResponseDto<UserResponse>> changePassword(UserChangePassword userChangePassword) {
        String idUser = userId();
        UserModel userModel = userRepository.findById(idUser).orElse(null);
        if (userModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Tài khoản không tồn tại",
                    StatusCodeEnum.USER1002
            );
        }

        String password = passwordEncoder.encode(userChangePassword.getNewPassword());
        userModel.setPassword(password);
        try {
            userModel = userRepository.save(userModel);
            return ResponseBuilder.okResponse(
                    "Đổi mật khẩu thành công",
                    mapper.map(userModel, UserResponse.class),
                    StatusCodeEnum.USER1001
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    "Lưu không thành công",
                    StatusCodeEnum.USER1001
            );
        }
    }

    public ResponseEntity<ResponseDto<UserResponse>> forgotPassword(UserForgotPassword userForgotPassword) {

        UserModel userModel = userRepository.findByEmail(userForgotPassword.getEmail()).orElse(null);
        if (userModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Tài khoản không tồn tại",
                    StatusCodeEnum.USER1002
            );
        }

        String password = generateTemporaryPassword();
        userModel.setPassword(passwordEncoder.encode(password));
        EmailRequest emailRequest = EmailRequest.builder()
                .subject(AppConstants.FORGOT)
                .channel("Email")
                .templateCode("FORGOT")
                .param(Map.of("name", userModel.getUsername(),
                        "verificationUrl", AppConstants.FORGOT_PASSWORD))
                .recipient(userModel.getEmail())
                .build();

        try {
            emailService.sendEmail(emailRequest);
            userModel = userRepository.save(userModel);
            return ResponseBuilder.okResponse(
                    "Đã gửi mật khẩu tạm thời thành công",
                    mapper.map(userModel, UserResponse.class),
                    StatusCodeEnum.USER1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    "Gửi mật khẩu thất bại",
                    StatusCodeEnum.USER1001
            );
        }
    }


    public ResponseEntity<ResponseDto<UserResponse>> getUserById(String id) {
        UserModel userModel = userRepository.findById(id).orElse(null);
        if (userModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Tài khoản không tồn tại",
                    StatusCodeEnum.USER1002
            );
        }

        return ResponseBuilder.okResponse(
                "Lấy thông tin tài khoản theo id thành công",
                mapper.map(userModel, UserResponse.class),
                StatusCodeEnum.USER1006
        );
    }

    private String generateTemporaryPassword() {
        return RandomStringUtils.random(10, true, true);
    }

    public String userId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return null; // Trả về null nếu chưa đăng nhập
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserModel userModel = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        return userModel.getId();
    }

    public ResponseEntity<ResponseDto<CountResponse>> getTotalUser(ShopTotalRequest totalRequest) {
        LocalDateTime start = Optional.ofNullable(totalRequest.getStartDate())
                .orElse(LocalDateTime.of(1970, 1, 1, 0, 0));
        LocalDateTime end = Optional.ofNullable(totalRequest.getEndDate())
                .orElse(LocalDateTime.now());
        int total = userRepository.countByCreatedAtBetween(totalRequest.getStartDate(), totalRequest.getEndDate());
        return ResponseBuilder.okResponse(
                "Lấy tổng số tài khoản trong thời gian thành công",
                CountResponse.builder().total(total).build(),
                StatusCodeEnum.USER1000
        );
    }

    public ResponseEntity<ResponseDto<UserResponse>> getByUser() {
        String idUser = userId();
        UserModel userModel = userRepository.findById(idUser).orElse(null);
        if (userModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy User",
                    StatusCodeEnum.USER1002
            );
        }

        return ResponseBuilder.okResponse(
                "Lấy thông tin user đã đăng nhập thành công",
                mapper.map(userModel, UserResponse.class),
                StatusCodeEnum.USER1000
        );
    }

    public ResponseEntity<ResponseDto<List<UserResponse>>> getListUser(PanigationRequest request) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createAt");
        if (request.getSort() != null && !request.getSort().isBlank()) {
            String fieldSort = request.getSort().replace("-", "");
            Sort.Direction direction = request.getSort().startsWith("-") ? Sort.Direction.DESC : Sort.Direction.ASC;
            sort = Sort.by(direction, fieldSort);
        }
        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit(), sort);
        // Tạo tiêu chí tìm kiếm
        Criteria criteria = new Criteria();
        if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
            criteria.orOperator(
                    Criteria.where("name").regex(request.getKeyword(), "i"),
                    Criteria.where("email").regex(request.getKeyword(), "i"),
                    Criteria.where("username").regex(request.getKeyword(), "i")
            );
        }

        // Thực hiện truy vấn
        Query query = new Query(criteria).with(pageable);
        List<UserModel> userModels = mongoTemplate.find(query, UserModel.class);
        long totalElements = mongoTemplate.count(query.skip(0).limit(0), UserModel.class);
        List<UserResponse> userResponses = userModels.stream()
                .map(userModel -> mapper.map(userModel, UserResponse.class)).collect(Collectors.toList());

        MetaData metaData = MetaData.builder()
                .totalPage((int) Math.ceil((double) totalElements / request.getLimit()))
                .pageSize(request.getLimit())
                .currentPage(request.getPage())
                .total(totalElements)
                .build();

        return ResponseBuilder.okResponse(
                "Lấy danh sách user thành công",
                userResponses,
                metaData,
                StatusCodeEnum.USER1000
        );
    }

    public ResponseEntity<ResponseDto<UserResponse>> blockAccount(String idUser) {
        UserModel userModel = userRepository.findById(idUser).orElse(null);
        if (userModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy tài khoản",
                    StatusCodeEnum.USER1002
            );
        }

        userModel.setStatusUserEnums(StatusUserEnums.DEACTIVE);
        try {
            userModel = userRepository.save(userModel);
            return ResponseBuilder.okResponse(
                    "Khóa tài khoản thành công",
                    mapper.map(userModel, UserResponse.class),
                    StatusCodeEnum.USER1003
            );
        } catch (Exception e) {
            log.error("Error blockAccount() - {}", e.getMessage());
            return ResponseBuilder.badRequestResponse(
                    "Lỗi khi khóa tài khoản",
                    StatusCodeEnum.USER1002
            );
        }
    }
}
