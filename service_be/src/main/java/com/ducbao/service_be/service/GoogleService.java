package com.ducbao.service_be.service;

import com.ducbao.common.model.builder.ResponseBuilder;
import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.common.model.enums.StatusCodeEnum;
import com.ducbao.common.model.enums.StatusShopEnums;
import com.ducbao.common.model.enums.StatusUserEnums;
import com.ducbao.common.util.Util;
import com.ducbao.service_be.model.dto.request.GoogleRequest;
import com.ducbao.service_be.model.dto.request.LoginRequest;
import com.ducbao.service_be.model.dto.response.LoginResponse;
import com.ducbao.service_be.model.dto.response.UserInfoResponse;
import com.ducbao.service_be.model.entity.ShopModel;
import com.ducbao.service_be.model.entity.UserModel;
import com.ducbao.service_be.repository.ShopRepository;
import com.ducbao.service_be.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ShopRepository shopRepository;

    @Value("${google.client-id}")
    private String googleId;

    @Value("${google.client-secret}")
    private String googleSecret;

    @Value("${google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${google.path}")
    private String googlePath;

    /**
     * Login sso with Google
     *
     * @param request
     * @return
     */
    public ResponseEntity<ResponseDto<LoginResponse>> loginGoogle(GoogleRequest request) {
        GoogleTokenResponse googleTokenResponse;
        try {
            googleTokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    new JacksonFactory(),
                    googlePath,
                    googleId,
                    googleSecret,
                    request.getCode(),
                    googleRedirectUri
            ).execute();
        } catch (IOException e) {
            log.error("Verify authorization code of Google failed: " + e.getMessage());
            return ResponseBuilder.badRequestResponse(
                    "Login với google thất bại",
                    StatusCodeEnum.USER1004
            );
        }

        String idToken = googleTokenResponse.getIdToken();
        GoogleIdTokenVerifier googleIdTokenVerifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList(googleId)).build();
        try {
            GoogleIdToken idTokenResponse = googleIdTokenVerifier.verify(idToken);
            GoogleIdToken.Payload payload = idTokenResponse.getPayload();
            String email = payload.getEmail();
            String idSocial = payload.getSubject();
            String fullName = (String) payload.get("name");
            String avatar = (String) payload.get("picture");
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");
            if (!Util.isNullOrEmpty(email)) {
                email = email.toLowerCase();
            }

            UserModel userModel = userRepository.findByEmail(email).orElse(null);

            if (userModel != null) {
                ShopModel shopModel = shopRepository.findByIdUser(userModel.getId());
                if (shopModel != null && shopModel.getStatusShopEnums() == StatusShopEnums.ACTIVE) {
                    userModel.setRole(List.of("OWNER"));
                    userModel = userRepository.save(userModel);
                    UserInfoResponse userInfoResponse = modelMapper.map(userModel, UserInfoResponse.class);
                    String accessToken = jwtService.generateTokenByIdUser(userModel.getId());
                    LoginResponse loginResponse = LoginResponse.builder()
                            .accessToken(accessToken)
                            .userInfoResponse(userInfoResponse)
                            .build();
                    log.info("Login google response - {}",loginResponse.toString());
                    return ResponseBuilder.okResponse(
                            "Đăng nhập với google thành công với cửa hàng đã tồn tại",
                            loginResponse,
                            StatusCodeEnum.USER1000
                    );
                } else if (shopModel != null && shopModel.getStatusShopEnums()== StatusShopEnums.DEACTIVE) {
                    return ResponseBuilder.badRequestResponse(
                            "Đăng nhập với tài khoản google không thành công do của hàng chưa được kích hoạt",
                            StatusCodeEnum.USER1004
                    );
                } else {
                    UserInfoResponse userInfoResponse = modelMapper.map(userModel, UserInfoResponse.class);
                    String accessToken = jwtService.generateTokenByIdUser(userModel.getId());
                    LoginResponse loginResponse = LoginResponse.builder()
                            .accessToken(accessToken)
                            .userInfoResponse(userInfoResponse)
                            .build();
                    log.info("Login google response - {}",loginResponse.toString());
                    return ResponseBuilder.okResponse(
                            "Đăng nhập với google thành công với tài khoản đã tồn tại",
                            loginResponse,
                            StatusCodeEnum.USER1000
                    );
                }
            } else {
                UserModel userModelNew = UserModel.builder()
                        .email(email)
                        .avatar(avatar)
                        .firstName(firstName)
                        .lastName(lastName)
                        .username(email)
                        .idSocial(idSocial)
                        .statusUserEnums(StatusUserEnums.ACTIVE)
                        .role(List.of("USER"))
                        .build();
                try {
                    userModelNew = userRepository.save(userModelNew);
                    UserInfoResponse userInfoResponse = modelMapper.map(userModelNew, UserInfoResponse.class);
                    String accessToken = jwtService.generateTokenByIdUser(userModelNew.getId());
                    LoginResponse loginResponse = LoginResponse.builder()
                            .accessToken(accessToken)
                            .userInfoResponse(userInfoResponse)
                            .build();
                    log.info("Login google response - {}",loginResponse.toString());
                    return ResponseBuilder.okResponse(
                            "Đăng nhập với google thành công",
                            loginResponse,
                            StatusCodeEnum.USER1000
                    );

                } catch (Exception e) {
                    log.error("Error Login Goolge save user: " + e.getMessage());
                    return ResponseBuilder.badRequestResponse(
                            "Lỗi khi lưu user trong login với google",
                            StatusCodeEnum.USER1004
                    );
                }
            }
        } catch (GeneralSecurityException | IOException e) {
            log.error("Verify id token of Google failed: " + e.getMessage());

            return ResponseBuilder.badRequestResponse(
                    "Đăng nhập với google thất bại",
                    StatusCodeEnum.USER1004
            );
        }
    }

}
