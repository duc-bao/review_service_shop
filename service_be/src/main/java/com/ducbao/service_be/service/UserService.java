package com.ducbao.service_be.service;

import com.ducbao.common.model.builder.ResponseBuilder;
import com.ducbao.common.model.constant.FileConstant;
import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.common.model.enums.StatusCodeEnum;
import com.ducbao.service_be.model.dto.request.UserRequest;
import com.ducbao.service_be.model.dto.response.UserResponse;
import com.ducbao.service_be.model.entity.UserModel;
import com.ducbao.service_be.model.mapper.CommonMapper;
import com.ducbao.service_be.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final FileService fileService;
    private final CommonMapper mapper;

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
                        mapper.map(userModel,UserResponse.class),
                        StatusCodeEnum.USER1005
                );
            }catch (Exception e) {
                return ResponseBuilder.badRequestResponse(
                        "Lưu tài khoản thất bại",
                        StatusCodeEnum.USER1001
                );
            }
    }

    public String userId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserModel userModel = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        return userModel.getId();
    }
}
