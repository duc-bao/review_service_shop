package com.ducbao.service_be.service;

import com.ducbao.common.model.builder.ResponseBuilder;
import com.ducbao.common.model.constant.FileConstant;
import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.common.model.enums.StatusCodeEnum;
import com.ducbao.service_be.model.constant.AppConstants;
import com.ducbao.service_be.model.dto.request.*;
import com.ducbao.service_be.model.dto.response.CountResponse;
import com.ducbao.service_be.model.dto.response.UserResponse;
import com.ducbao.service_be.model.entity.UserModel;
import com.ducbao.service_be.model.mapper.CommonMapper;
import com.ducbao.service_be.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final FileService fileService;
    private final CommonMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

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
        }catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    "Gửi mật khẩu thất bại",
                    StatusCodeEnum.USER1001
            );
        }
    }


    public ResponseEntity<ResponseDto<UserResponse>> getUserById(String id){
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
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserModel userModel = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        return userModel.getId();
    }

    public ResponseEntity<ResponseDto<CountResponse>> getTotalUser(ShopTotalRequest totalRequest) {
        int total = userRepository.countByCreatedAtBetween(totalRequest.getStartDate(), totalRequest.getEndDate());
        return ResponseBuilder.okResponse(
                "Lấy tổng số tài khoản trong thời gian thành công",
                CountResponse.builder().total(total).build(),
                StatusCodeEnum.USER1000
        );
    }
}
