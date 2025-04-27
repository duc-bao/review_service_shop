package com.ducbao.service_be.service;

import com.ducbao.common.model.builder.ResponseBuilder;
import com.ducbao.common.model.dto.MetaData;
import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.common.model.enums.StatusCodeEnum;
import com.ducbao.service_be.model.dto.request.FavoriteRequest;
import com.ducbao.service_be.model.dto.request.ShopTotalRequest;
import com.ducbao.service_be.model.dto.response.CountResponse;
import com.ducbao.service_be.model.dto.response.FavoriteResponse;
import com.ducbao.service_be.model.entity.FavoriteModel;
import com.ducbao.service_be.model.entity.ShopModel;
import com.ducbao.service_be.model.mapper.CommonMapper;
import com.ducbao.service_be.repository.FavoriteRepository;
import com.ducbao.service_be.repository.ShopRepository;
import com.ducbao.service_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final CommonMapper mapper;

    public ResponseEntity<ResponseDto<FavoriteResponse>> addFavorite(FavoriteRequest favoriteRequest) {
        String idUser = userService.userId();
        if (favoriteRepository.existsByIdUserAndIdShop(idUser, favoriteRequest.getIdShop())) {
            FavoriteModel favoriteModel = favoriteRepository.findByIdUserAndIdShop(idUser, favoriteRequest.getIdShop());
            return ResponseBuilder.okResponse(
                    "Đã tồn tại yêu thích cửa hàng này",
                    mapper.map(favoriteModel, FavoriteResponse.class),
                    StatusCodeEnum.FAVORITE1000
            );
        }

        FavoriteModel favoriteModel = mapper.map(favoriteRequest, FavoriteModel.class);
        favoriteModel.setIdUser(idUser);

        try {
            favoriteModel = favoriteRepository.save(favoriteModel);
            return ResponseBuilder.okResponse(
                    "Thêm cửa hàng yêu thích thành công",
                    mapper.map(favoriteModel, FavoriteResponse.class),
                    StatusCodeEnum.FAVORITE1000
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseBuilder.badRequestResponse(
                    "Thêm yêu thích thất bại",
                    StatusCodeEnum.FAVORITE1001
            );
        }
    }

    public ResponseEntity<ResponseDto<FavoriteResponse>> getFavoriteById(String id) {
        FavoriteModel favoriteModel = favoriteRepository.findById(id).orElse(null);
        if (favoriteModel == null) {
            return ResponseBuilder.okResponse(
                    "Không tìm thấy cửa hàng yêu thích",
                    StatusCodeEnum.FAVORITE1002
            );
        }
        return ResponseBuilder.okResponse(
                "Lấy thông tin cửa hàng yêu thích thành công",
                mapper.map(favoriteModel, FavoriteResponse.class),
                StatusCodeEnum.FAVORITE1000
        );
    }

    public ResponseEntity<ResponseDto<List<FavoriteResponse>>> getListFavorite(String s, int limit, int page) {
        String idUser = userService.userId();
        Sort sort = Sort.by(Sort.Direction.DESC, s);
        Pageable pageable = PageRequest.of(page - 1, limit, sort);

        Page<FavoriteModel> favoriteModels = favoriteRepository.findAllByIdUser(idUser, pageable);
        List<FavoriteModel> favoriteModelList = favoriteModels.getContent();
        List<FavoriteResponse> favoriteResponseList = favoriteModelList.stream().map(
                favoriteModel -> {
                    return shopRepository.findById(favoriteModel.getIdShop())
                            .filter(ShopModel::isVery)
                            .map(shopModel -> {
                                FavoriteResponse favoriteModel1 = FavoriteResponse.builder()
                                        .idShop(shopModel.getId())
                                        .idUser(idUser)
                                        .build();
                                return favoriteModel1;
                            }).orElse(null);
                }
        ).filter(Objects::nonNull).collect(Collectors.toList());

        MetaData metaData = MetaData.builder()
                .currentPage(page)
                .total(favoriteModels.getTotalElements())
                .pageSize(limit)
                .totalPage(favoriteModels.getTotalPages())
                .build();

        return ResponseBuilder.okResponse(
                "Lấy danh sách yêu thích cửa hàng thành công",
                favoriteResponseList,
                metaData,
                StatusCodeEnum.FAVORITE1000
        );
    }

    public ResponseEntity<ResponseDto<FavoriteResponse>> deleteFavorite(FavoriteRequest favoriteRequest) {
        String idUser = userService.userId();
        FavoriteModel favoriteModel = favoriteRepository.findByIdUserAndIdShop(idUser,favoriteRequest.getIdShop());
        if (favoriteModel == null) {
            return ResponseBuilder.okResponse(
                    "Không tìm thấy cửa hàng yêu thích",
                    StatusCodeEnum.FAVORITE1002
            );
        }
        try {
            favoriteRepository.delete(favoriteModel);
            return ResponseBuilder.okResponse(
                    "Xóa thành công cửa hàng yêu thích",
                    mapper.map(favoriteModel, FavoriteResponse.class),
                    StatusCodeEnum.FAVORITE1000
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseBuilder.badRequestResponse(
                    "Không thể xóa cửa hàng yêu thích",
                    StatusCodeEnum.FAVORITE1001
            );
        }
    }

    public ResponseEntity<ResponseDto<CountResponse>> getTotalFavorite(ShopTotalRequest request) {
        String idUser = userService.userId();
        ShopModel shopModel  = shopRepository.findByIdUser(idUser);
        if(shopModel == null){
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy cửa hàng",
                    StatusCodeEnum.SHOP1003
            );
        }
        LocalDateTime startDate = Optional.ofNullable(request.getStartDate())
                .orElse(LocalDateTime.of(1970, 1, 1, 0, 0));
        LocalDateTime endDate = Optional.ofNullable(request.getEndDate())
                .orElse(LocalDateTime.now());
        int  total = favoriteRepository.countByCreatedAtBetweenAndIdShop(startDate, endDate, shopModel.getId());
        return ResponseBuilder.okResponse(
                "Lấy tổng số đánh giá theo cửa hàng trong thời gian thành công",
                CountResponse.builder().total(total).build(),
                StatusCodeEnum.FAVORITE1000
        );
    }

}
