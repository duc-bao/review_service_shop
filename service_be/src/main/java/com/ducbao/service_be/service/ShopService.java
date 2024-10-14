package com.ducbao.service_be.service;

import com.ducbao.common.model.builder.ResponseBuilder;
import com.ducbao.common.model.constant.FileConstant;
import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.common.model.entity.OpenTimeBaseModel;
import com.ducbao.common.model.enums.StatusCodeEnum;
import com.ducbao.common.model.enums.StatusShopEnums;
import com.ducbao.service_be.model.constant.AppConstants;
import com.ducbao.service_be.model.dto.request.EmailRequest;
import com.ducbao.service_be.model.dto.request.ShopRequest;
import com.ducbao.service_be.model.dto.response.EmailResponse;
import com.ducbao.service_be.model.dto.response.OpenTimeResponse;
import com.ducbao.service_be.model.dto.response.ShopGetResponse;
import com.ducbao.service_be.model.dto.response.ShopResponse;
import com.ducbao.service_be.model.entity.OpenTimeModel;
import com.ducbao.service_be.model.entity.ShopModel;
import com.ducbao.service_be.model.entity.UserModel;
import com.ducbao.service_be.model.mapper.CommonMapper;
import com.ducbao.service_be.repository.OpenTimeRepository;
import com.ducbao.service_be.repository.ShopRepository;
import com.ducbao.service_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopService {
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CommonMapper mapper;
    private final FileService fileService;
    private final OpenTimeRepository openTimeRepository;
    private final EmailService emailService;

    public ResponseEntity<ResponseDto<ShopResponse>> createShop(ShopRequest shopRequest) {
        String idUser = userService.userId();
        UserModel userModel = userRepository.findById(idUser).orElse(null);

        if (userModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Người dùng không tồn tại",
                    StatusCodeEnum.USER1002
            );
        }

        ShopModel shopModel = mapper.map(shopRequest, ShopModel.class);
        shopModel.setIdUser(userModel.getId());
        shopModel.setStatusShopEnums(StatusShopEnums.DEACTIVE);

        List<OpenTimeModel> openTimeBaseModelList = shopRequest.getOpenTimeRequests().stream().map(
                openTimeRequest -> mapper.map(openTimeRequest, OpenTimeModel.class)).collect(Collectors.toList());
        openTimeBaseModelList = openTimeRepository.saveAll(openTimeBaseModelList);
        log.info(openTimeBaseModelList.toString());
        shopModel.setListIdOpenTime(openTimeBaseModelList.stream().map(OpenTimeBaseModel::getId).collect(Collectors.toList()))  ;
        try {
            shopModel = shopRepository.save(shopModel);
            return ResponseBuilder.okResponse(
                    "Tạo cửa hàng thành công vui lòng đợi admin xác nhận cửa hàng",
                    mapper.map(shopModel, ShopResponse.class),
                    StatusCodeEnum.SHOP1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    "Lưu cửa hàng thất bại",
                    StatusCodeEnum.SHOP1001
            );
        }

    }

    public ResponseEntity<ResponseDto<ShopResponse>> updateShop(ShopRequest shopRequest) {
        String idUser = userService.userId();
        ShopModel shopModel = shopRepository.findByIdUser(idUser);

        if (shopModel == null){
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy cửa hàng",
                    StatusCodeEnum.SHOP1003
            );
        }
        List<String> mediaUrls = shopModel.getMediaUrls();
        if(mediaUrls != null && mediaUrls.size() > 0){
            mediaUrls.addAll(shopRequest.getMediaUrls());
        }
        shopModel.setMediaUrls(mediaUrls);
        shopModel = mapper.map(shopRequest, ShopModel.class);
        try {
            shopModel = shopRepository.save(shopModel);
            return ResponseBuilder.okResponse(
                    "Cập nhật cửa hàng thành công",
                    mapper.map(shopModel, ShopResponse.class),
                    StatusCodeEnum.SHOP1000
            );
        }catch (Exception e){
            return ResponseBuilder.badRequestResponse(
                    "Cập nhật cửa hàng thất bại",
                    StatusCodeEnum.SHOP1001
            );
        }
    }

    public ResponseEntity<ResponseDto<ShopResponse>> activeShop(String id) {
        ShopModel shopModel = shopRepository.findById(id).orElse(null);
        if (shopModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy cửa hàng",
                    StatusCodeEnum.SHOP1003
            );
        }

        if(shopModel.isVery() || shopModel.getStatusShopEnums().equals("ACTIVE")){
            return ResponseBuilder.okResponse(
                    "Cửa hàng đã được kích hoạt",
                    mapper.map(shopModel, ShopResponse.class),
                    StatusCodeEnum.SHOP1000
            );
        }
        UserModel userModel = userRepository.findById(shopModel.getIdUser()).orElse(null);
        if (userModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy người dùng",
                    StatusCodeEnum.USER1002
            );
        }

        userModel.setRole(List.of("OWNER"));
        shopModel.setStatusShopEnums(StatusShopEnums.ACTIVE);
        shopModel.setVery(true);
        try {
            shopModel = shopRepository.save(shopModel);
            EmailRequest emailRequest = EmailRequest.builder()
                    .channel("email")
                    .recipient(userModel.getEmail())
                    .templateCode("ACTIVE")
                    .param(Map.of("name", shopModel.getName(),
                            "verificationUrl", AppConstants.LINK_SHOP))
                    .subject(AppConstants.SUBJECT_REGISTER)
                    .build();
            emailService.sendEmail(emailRequest);

            return ResponseBuilder.okResponse(
                    "Kích hoạt cửa hàng thành công",
                    mapper.map(shopModel, ShopResponse.class),
                    StatusCodeEnum.SHOP1000
            );
        }catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    "Lưu thất bại ",
                    StatusCodeEnum.SHOP1001
            );
        }
    }

    public ResponseEntity<ResponseDto<ShopGetResponse>> getShopById(String id) {
        ShopModel shopModel = shopRepository.findById(id).orElse(null);
        if (shopModel == null){
            return  ResponseBuilder.badRequestResponse(
                    "Cửa hàng không tồn tại",
                    StatusCodeEnum.SHOP1003
            );
        }

        ShopGetResponse shopGetResponse = mapper.map(shopModel, ShopGetResponse.class);
        List<OpenTimeModel> openTimeModels = openTimeRepository.findAllById(shopModel.getListIdOpenTime());
        List<OpenTimeResponse> openTimeResponses = openTimeModels.stream().map(
                openTimeModel -> mapper.map(openTimeModel, OpenTimeResponse.class)).collect(Collectors.toList());

        shopGetResponse.setListOpenTimes(openTimeResponses);

        return ResponseBuilder.okResponse(
                "Lấy thông tin cửa hàng theo id thành công",
                shopGetResponse,
                StatusCodeEnum.SHOP1004
        );
    }

    public ResponseEntity<ResponseDto<String>> uploadImagme(MultipartFile file) {
        String idUser = userService.userId();
        String url = fileService.upload(file, idUser, FileConstant.IMAGE_SHOP);
        return ResponseBuilder.okResponse(
                "Tải ảnh lên thành công",
                url,
                StatusCodeEnum.SHOP1002
        );
    }
}
