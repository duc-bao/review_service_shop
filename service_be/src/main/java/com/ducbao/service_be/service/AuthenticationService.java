package com.ducbao.service_be.service;

import com.ducbao.common.model.builder.ResponseBuilder;
import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.common.model.enums.StatusCodeEnum;
import com.ducbao.common.model.enums.StatusUserEnums;
import com.ducbao.service_be.model.constant.AppConstants;
import com.ducbao.service_be.model.dto.request.EmailRequest;
import com.ducbao.service_be.model.dto.request.LoginRequest;
import com.ducbao.service_be.model.dto.request.ResgisterRequest;
import com.ducbao.service_be.model.dto.response.LoginResponse;
import com.ducbao.service_be.model.dto.response.UserInfoResponse;
import com.ducbao.service_be.model.entity.UserModel;
import com.ducbao.service_be.model.mapper.CommonMapper;
import com.ducbao.service_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CommonMapper commonMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public ResponseEntity<ResponseDto<LoginResponse>> login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(authentication);
            String userId = jwtService.getUserIdFromJWT(token);

            UserModel userModel = userRepository.findById(userId).orElse(null);
            UserInfoResponse userInfoResponse = commonMapper.map(userModel, UserInfoResponse.class);
            LoginResponse loginResponse = LoginResponse.builder()
                    .accessToken(token)
                    .userInfoResponse(userInfoResponse)
                    .build();

            if ("ACTIVE".equals(userModel.getStatusUserEnums().toString())) {
                return ResponseBuilder.okResponse(
                        "Đăng nhập thành công",
                        loginResponse,
                        StatusCodeEnum.LOGIN1000
                );
            }
            return ResponseBuilder.badRequestResponse(
                    "Đăng nhập thất bại do tài khoản chưa được kích hoạt",
                    StatusCodeEnum.LOGIN1001
            );
        }
        return ResponseBuilder.badRequestResponse(
                "Tên đăng nhập hoặc mật khẩu không chính xác",
                null,
                StatusCodeEnum.LOGIN1001
        );
    }

    public ResponseEntity<ResponseDto<Void>> register(ResgisterRequest registerRequest) {
        UserModel userModel = commonMapper.map(registerRequest, UserModel.class);
        if (userRepository.existsByEmail(userModel.getEmail())) {
            return ResponseBuilder.badRequestResponse(
                    "Email đã tồn tại",
                    StatusCodeEnum.LOGIN1002
            );
        }

        if (userRepository.existsByUsername(userModel.getUsername())) {
            return ResponseBuilder.badRequestResponse(
                    "Username đã tồn tại",
                    StatusCodeEnum.LOGIN1003
            );
        }

        if (userRepository.existsByPhone(userModel.getPhone())) {
            return ResponseBuilder.badRequestResponse(
                    "Số điện thoại đã tồn tại đã tồn tại",
                    StatusCodeEnum.LOGIN1004
            );
        }

        userModel.setActiveCode(activationCode());
        userModel.setStatusUserEnums(StatusUserEnums.DEACTIVE);
        String endecodePassword = passwordEncoder.encode(userModel.getPassword());
        userModel.setPassword(endecodePassword);
        userModel.setRole(List.of("USER"));
        EmailRequest emailRequest = EmailRequest.builder()
                .channel("email")
                .recipient(userModel.getEmail())
                .templateCode("REGISTER")
                .param(Map.of("name", userModel.getUsername(),
                        "verificationUrl", AppConstants.LINK_ACTIVE_ACCOUNT + userModel.getActiveCode()))
                .subject(AppConstants.SUBJECT_REGISTER)
                .build();
        // Sử dụng bất đồng bộ

//        emailService.sendEmail(emailRequest)
        try {
            userRepository.save(userModel);
            emailService.sendEmail(emailRequest);
            return ResponseBuilder.okResponse(
                    "Đăng kí tài khoản thành công vui lòng kiểm tra email để kích hoạt tài khoản",
                    StatusCodeEnum.USER1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    "Lưu người dùng không thành công",
                    StatusCodeEnum.USER1001
            );
        }


    }

    public ResponseEntity<ResponseDto<Void>> registerWithShop(ResgisterRequest registerRequest) {
        UserModel userModel = commonMapper.map(registerRequest, UserModel.class);
        if (userRepository.existsByEmail(userModel.getEmail())) {
            return ResponseBuilder.badRequestResponse(
                    "Email đã tồn tại",
                    StatusCodeEnum.LOGIN1002
            );
        }

        if (userRepository.existsByUsername(userModel.getUsername())) {
            return ResponseBuilder.badRequestResponse(
                    "Username đã tồn tại",
                    StatusCodeEnum.LOGIN1003
            );
        }

        if (userRepository.existsByPhone(userModel.getPhone())) {
            return ResponseBuilder.badRequestResponse(
                    "Số điện thoại đã tồn tại đã tồn tại",
                    StatusCodeEnum.LOGIN1004
            );
        }

        userModel.setActiveCode(activationCode());
        userModel.setStatusUserEnums(StatusUserEnums.DEACTIVE);
        String endecodePassword = passwordEncoder.encode(userModel.getPassword());
        userModel.setPassword(endecodePassword);
//        userModel.setRole(List.of("OWNER"));
//        EmailRequest emailRequest = EmailRequest.builder()
//                .channel("email")
//                .recipient(userModel.getEmail())
//                .templateCode("REGISTER")
//                .param(Map.of("name", userModel.getUsername(),
//                        "verificationUrl", AppConstants.LINK_ACTIVE_ACCOUNT + userModel.getActiveCode()))
//                .subject(AppConstants.SUBJECT_REGISTER)
//                .build();
        // Sử dụng bất đồng bộ

//        emailService.sendEmail(emailRequest)
        try {
            userRepository.save(userModel);
//            emailService.sendEmail(emailRequest);
            return ResponseBuilder.okResponse(
                    "Đăng kí tài khoản thành công vui lòng kiểm tra email để kích hoạt tài khoản",
                    StatusCodeEnum.USER1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    "Lưu người dùng không thành công",
                    StatusCodeEnum.USER1001
            );
        }


    }
    public ResponseEntity<ResponseDto<UserInfoResponse>> activeAcount(String code) {
        UserModel userModel = userRepository.findByActiveCode(code);
        if (userModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy tài khoản",
                    StatusCodeEnum.USER1002
            );
        }

        userModel.setStatusUserEnums(StatusUserEnums.ACTIVE);
        userRepository.save(userModel);
        return ResponseBuilder.badRequestResponse(
                "Kích hoạt tài khoản thành công",
                commonMapper.map(userModel, UserInfoResponse.class),
                StatusCodeEnum.USER1003
        );
    }
    public ResponseEntity<ResponseDto<Boolean>> existEmail(String email) {
        boolean isExist = userRepository.existsByEmail(email);
        if (isExist){
            return ResponseBuilder.badRequestResponse(
                    "Đã tồn tại email",
                    true,
                    StatusCodeEnum.USER1007
            );
        }
        return ResponseBuilder.okResponse(
                "Email chưa tồn tại",
                false,
                StatusCodeEnum.USER1000
        );
    }

    public ResponseEntity<ResponseDto<Boolean>> exitsUsername(String username) {
        boolean isExist = userRepository.existsByUsername(username);
        if (isExist){
            return ResponseBuilder.badRequestResponse(
                    "Đã tồn tại username",
                    true,
                    StatusCodeEnum.USER1007
            );
        }
        return ResponseBuilder.okResponse(
                "Username chưa tồn tại",
                false,
                StatusCodeEnum.USER1000
        );
    }
    // Tạo mã kích hoạt
    private String activationCode() {
        return UUID.randomUUID().toString();
    }
}
