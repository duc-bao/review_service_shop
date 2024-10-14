package com.ducbao.service_be.service;

import com.ducbao.common.model.builder.ResponseBuilder;
import com.ducbao.common.model.constant.FileConstant;
import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.common.model.enums.StatusCodeEnum;
import com.ducbao.common.model.enums.StatusShopEnums;
import com.ducbao.service_be.model.dto.request.ShopRequest;
import com.ducbao.service_be.model.dto.response.ShopResponse;
import com.ducbao.service_be.model.entity.ShopModel;
import com.ducbao.service_be.model.entity.UserModel;
import com.ducbao.service_be.model.mapper.CommonMapper;
import com.ducbao.service_be.repository.ShopRepository;
import com.ducbao.service_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopService {
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CommonMapper mapper;
    private final FileService fileService;

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
