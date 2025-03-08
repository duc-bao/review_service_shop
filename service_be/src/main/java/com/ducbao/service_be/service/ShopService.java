package com.ducbao.service_be.service;

import com.ducbao.common.model.builder.ResponseBuilder;
import com.ducbao.common.model.constant.FileConstant;
import com.ducbao.common.model.dto.MetaData;
import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.common.model.entity.*;
import com.ducbao.common.model.enums.StateServiceEnums;
import com.ducbao.common.model.enums.StatusCodeEnum;
import com.ducbao.common.model.enums.StatusShopEnums;
import com.ducbao.common.model.enums.StatusUserEnums;
import com.ducbao.common.util.Util;
import com.ducbao.service_be.model.constant.AppConstants;
import com.ducbao.service_be.model.dto.request.*;
import com.ducbao.service_be.model.dto.response.*;
import com.ducbao.service_be.model.entity.*;
import com.ducbao.service_be.model.mapper.CommonMapper;
import com.ducbao.service_be.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
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
    private final ServiceRepository serviceRepository;
    private final ReviewRepository reviewRepository;
    private final CategoryRepository categoryRepository;
    private final ElasticsearchOperations elasticsearchOperations;
    private final MongoTemplate mongoTemplate;

    public ResponseEntity<ResponseDto<ShopResponse>> createShop(ShopRequest shopRequest) {
//        String idUser = userService.userId();
        UserModel userModel = userRepository.findByEmail(shopRequest.getEmail()).orElse(null);

        if (userModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Người dùng không tồn tại",
                    StatusCodeEnum.USER1002
            );
        }
        ShopModel shopModel1 = shopRepository.findByIdUser(userModel.getId());
        if (shopModel1 != null) {
            return ResponseBuilder.badRequestResponse(
                    "Tài khoản này đã tạo 1 cửa hàng rồi bạn không được phép tạo thêm cửa hàng",
                    StatusCodeEnum.USER1002
            );
        }

        ShopModel shopModel = mapper.map(shopRequest, ShopModel.class);
        shopModel.setIdUser(userModel.getId());
        shopModel.setStatusShopEnums(StatusShopEnums.DEACTIVE);

        if (!shopRequest.getOpenTimeRequests().isEmpty()) {
            List<OpenTimeModel> openTimeBaseModelList = shopRequest.getOpenTimeRequests().stream().map(
                    openTimeRequest -> mapper.map(openTimeRequest, OpenTimeModel.class)).collect(Collectors.toList());
            openTimeBaseModelList = openTimeRepository.saveAll(openTimeBaseModelList);
            log.info(openTimeBaseModelList.toString());
            shopModel.setListIdOpenTime(openTimeBaseModelList.stream().map(OpenTimeBaseModel::getId).collect(Collectors.toList()));
        }
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

        if (shopModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy cửa hàng",
                    StatusCodeEnum.SHOP1003
            );
        }
        if (shopRequest.getMediaUrls() != null && !shopRequest.getMediaUrls().isEmpty()) {
            List<String> updatedMediaUrls = new ArrayList<>(shopModel.getMediaUrls()); // Giữ ảnh cũ
            updatedMediaUrls.addAll(shopRequest.getMediaUrls()); // Thêm ảnh mới vào danh sách
            shopModel.setMediaUrls(updatedMediaUrls);
        }
        mapper.maptoObject(shopRequest, shopModel);
        try {
            shopModel = shopRepository.save(shopModel);
            return ResponseBuilder.okResponse(
                    "Cập nhật cửa hàng thành công",
                    mapper.map(shopModel, ShopResponse.class),
                    StatusCodeEnum.SHOP1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    "Cập nhật cửa hàng thất bại",
                    StatusCodeEnum.SHOP1001
            );
        }
    }

    public ResponseEntity<ResponseDto<List<OpenTimeResponse>>> updateOpenTime(List<OpenTimeRequest> listOpenTimeRequest) {
        String idUser = userService.userId();
        ShopModel shopModel = shopRepository.findByIdUser(idUser);
        if (shopModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy cửa hàng",
                    StatusCodeEnum.SHOP1003
            );
        }

        List<String> idOpenTime = shopModel.getListIdOpenTime();
        List<OpenTimeModel> updateOpenTime = new ArrayList<>();
        for (OpenTimeRequest openTimeRequest : listOpenTimeRequest) {
            OpenTimeModel existingOpenTime = null;
            if (idOpenTime != null && !idOpenTime.isEmpty()) {
                for (String id : idOpenTime) {
                    OpenTimeModel openTime = openTimeRepository.findById(id).orElse(null);
                    if (openTime != null && openTime.getDayOfWeekEnum() == openTimeRequest.getDayOfWeekEnum()) {
                        existingOpenTime = openTime;
                        break;
                    }
                }
            }

            OpenTimeModel openTimeModel;
            if (existingOpenTime != null) {
                // Cập nhật OpenTime hiện có
                openTimeModel = existingOpenTime;
                openTimeModel.setCloseTime(openTimeRequest.getCloseTime());
                openTimeModel.setOpenTime(openTimeRequest.getOpenTime());
                openTimeModel.setDayOff(openTimeRequest.isDayOff());
            } else {
                // Tạo mới OpenTime
                openTimeModel = OpenTimeModel.builder()
                        .id(UUID.randomUUID().toString())
                        .dayOfWeekEnum(openTimeRequest.getDayOfWeekEnum())
                        .isDayOff(openTimeRequest.isDayOff())
                        .openTime(openTimeRequest.getOpenTime())
                        .closeTime(openTimeRequest.getCloseTime())
                        .build();
                idOpenTime.add(openTimeModel.getId());
            }
            updateOpenTime.add(openTimeModel);
        }
        updateOpenTime = openTimeRepository.saveAll(updateOpenTime);
        shopModel.setListIdOpenTime(idOpenTime);
        shopRepository.save(shopModel);

        return ResponseBuilder.okResponse(
                "Cập nhật thời gian hoạt động của cửa hàng thành công",
                updateOpenTime.stream().map(updateOpenTimes -> mapper.map(updateOpenTimes, OpenTimeResponse.class)).collect(Collectors.toList()),
                StatusCodeEnum.SHOP1005
        );
    }

    public ResponseEntity<ResponseDto<List<OpenTimeResponse>>> getListOpenTime(String idShop) {
        ShopModel shopModel = shopRepository.findById(idShop).orElse(null);
        if (shopModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Cửa hàng không tồn tại",
                    StatusCodeEnum.SHOP1003
            );
        }

        List<String> idOpenTime = shopModel.getListIdOpenTime();
        List<OpenTimeModel> updateOpenTime = new ArrayList<>();
        for (String id : idOpenTime) {
            OpenTimeModel openTimeModel = openTimeRepository.findById(id).orElse(null);
            updateOpenTime.add(openTimeModel);
        }
        updateOpenTime.sort((o1, o2) -> o1.getDayOfWeekEnum().ordinal() - o2.getDayOfWeekEnum().ordinal());
        return ResponseBuilder.okResponse(
                "Lấy danh sách thời gian của cửa hàng thành công",
                updateOpenTime.stream().map(
                        updateOpenTimes -> mapper.map(updateOpenTimes, OpenTimeResponse.class)
                ).collect(Collectors.toList()),
                StatusCodeEnum.SHOP1006
        );
    }
    public ResponseEntity<ResponseDto<List<OpenTimeResponse>>> getListOpenTimeByShop() {
        String idUser = userService.userId();
        ShopModel shopModel = shopRepository.findByIdUser(idUser);
        if (shopModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Cửa hàng không tồn tại",
                    StatusCodeEnum.SHOP1003
            );
        }

        List<String> idOpenTime = shopModel.getListIdOpenTime();
        List<OpenTimeModel> updateOpenTime = new ArrayList<>();
        for (String id : idOpenTime) {
            OpenTimeModel openTimeModel = openTimeRepository.findById(id).orElse(null);
            updateOpenTime.add(openTimeModel);
        }
        updateOpenTime.sort((o1, o2) -> o1.getDayOfWeekEnum().ordinal() - o2.getDayOfWeekEnum().ordinal());
        return ResponseBuilder.okResponse(
                "Lấy danh sách thời gian của cửa hàng thành công",
                updateOpenTime.stream().map(
                        updateOpenTimes -> mapper.map(updateOpenTimes, OpenTimeResponse.class)
                ).collect(Collectors.toList()),
                StatusCodeEnum.SHOP1006
        );
    }
    public ResponseEntity<ResponseDto<ShopResponse>> activeShop(String id) {
        ShopModel shopModel = shopRepository.findById(id).orElse(null);
        if (shopModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy cửa hàng",
                    StatusCodeEnum.SHOP1003
            );
        }

        if (shopModel.isVery() && shopModel.getStatusShopEnums().equals("ACTIVE")) {
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
//        if (!shopModel.isOwner()) {
//            shopModel.setStatusShopEnums(StatusShopEnums.ACTIVE);
//            shopModel = shopRepository.save(shopModel);
//            return ResponseBuilder.okResponse("Kích hoạt cửa hàng thành công với người tạo cửa hàng là ẩn danh",
//                    mapper.map(shopModel, ShopResponse.class),
//                    StatusCodeEnum.SHOP1000);
//        }
//        userModel.setRole(List.of("OWNER"));
        userModel.setStatusUserEnums(StatusUserEnums.ACTIVE);
        userModel.setRole(List.of("OWNER"));
        shopModel.setStatusShopEnums(StatusShopEnums.ACTIVE);
        shopModel.setVery(true);

        try {
            userRepository.save(userModel);
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
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    "Lưu thất bại ",
                    StatusCodeEnum.SHOP1001
            );
        }
    }

    public ResponseEntity<ResponseDto<ShopResponse>> updateActiveShop(String id, VerifyShopRequest verifyShopRequest) {
        ShopModel shopModel = shopRepository.findById(id).orElse(null);
        if (shopModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy cửa hàng",
                    StatusCodeEnum.SHOP1003
            );
        }
        mapper.maptoObject(verifyShopRequest, shopModel);
        String idUser = userService.userId();
        shopModel.setIdUser(idUser);
        try {
            shopModel = shopRepository.save(shopModel);
            return ResponseBuilder.okResponse(
                    "Cập nhật chủ cửa hàng mới thành công vui lòng chờ admin xác nhận",
                    mapper.map(shopModel, ShopResponse.class),
                    StatusCodeEnum.SHOP1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    "Lưu thất bại ",
                    StatusCodeEnum.SHOP1001
            );
        }
    }

    public ResponseEntity<ResponseDto<ShopGetResponse>> getShopById(String id) {
        ShopModel shopModel = shopRepository.findById(id).orElse(null);
        if (shopModel == null) {
            return ResponseBuilder.badRequestResponse(
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

    public ResponseEntity<ResponseDto<ShopGetResponse>> getShop() {
        String idUser = userService.userId();
        ShopModel shopModel = shopRepository.findByIdUser(idUser);
        if(shopModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thâ cửa hàng",
                    StatusCodeEnum.SHOP1003
            );
        }
        try {
            ShopGetResponse shopResponse = mapper.map(shopModel, ShopGetResponse.class);
            List<OpenTimeModel> openTimeModels = openTimeRepository.findAllById(shopModel.getListIdOpenTime());
            List<OpenTimeResponse> openTimeResponses = openTimeModels.stream()
                    .map(openTimeModel -> mapper.map(openTimeModel, OpenTimeResponse.class)).collect(Collectors.toList());
            shopResponse.setListOpenTimes(openTimeResponses);
            return ResponseBuilder.okResponse(
                    "Lấy thông tin cửa hàng thành công",
                    shopResponse,
                    StatusCodeEnum.SHOP1004
            );
        }catch (Exception e) {
            log.error("Error getShop() - {}", e.getMessage());
            return ResponseBuilder.badRequestResponse(
                    "Xảy ra lỗi khi lấy thông tin cửa hàng",
                    StatusCodeEnum.SHOP1003
            );
        }
    }

//    public ResponseEntity<ResponseDto<List<String>>> uploadMultiFile(MultipartFile[] files) {

    /// /        String idUser = userService.userId();
//        String idUser = "123";
//        List<String> mediaUrls = new ArrayList<>();
//
//        for (MultipartFile file : files) {
//            String url = fileService.upload(file, idUser, FileConstant.IMAGE_SHOP);
//            mediaUrls.add(url);
//        }
//        return ResponseBuilder.okResponse(
//                "Tải nhiều ảnh của cửa hàng lên thành công",
//                mediaUrls,
//                StatusCodeEnum.SHOP1002
//        );
//
//    }

//    public ResponseEntity<ResponseDto<String>> uploadImagme(MultipartFile file) {
//        String idUser = userService.userId();
//        String url = fileService.upload(file, idUser, FileConstant.IMAGE_SHOP);
//        return ResponseBuilder.okResponse(
//                "Tải ảnh lên thành công",
//                url,
//                StatusCodeEnum.SHOP1002
//        );
//    }
    public ResponseEntity<ResponseDto<List<String>>> uploadMultipartFile(MultipartFile[] files, String email) {
        UserModel userModel = userRepository.findByEmail(email).orElse(null);
        if (userModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy tài khoản",
                    StatusCodeEnum.USER1002
            );
        }

        List<String> mediaUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = fileService.upload(file, userModel.getId(), FileConstant.IMAGE_SHOP);
            mediaUrls.add(url);
        }
        return ResponseBuilder.okResponse(
                "Tải nhiều ảnh của cửa hàng lên thành công",
                mediaUrls,
                StatusCodeEnum.SHOP1002
        );
    }

    public ResponseEntity<ResponseDto<String>> uploadAvatar(MultipartFile file, String email) {
        UserModel userModel = userRepository.findByEmail(email).orElse(null);
        if (userModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy user",
                    StatusCodeEnum.USER1002
            );
        }
        String url = fileService.upload(file, userModel.getId(), FileConstant.IMAGE_SHOP);
        return ResponseBuilder.okResponse(
                "Tải ảnh lên thành công",
                url,
                StatusCodeEnum.SHOP1002
        );
    }

    public ResponseEntity<ResponseDto<ServiceResponse>> createService(ServiceRequest serviceRequest) {
        String idUser = userService.userId();
        ShopModel shopModel = shopRepository.findByIdUser(idUser);

        if (shopModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Cửa hàng không tồn tại",
                    StatusCodeEnum.SHOP1003
            );
        }

        ServiceModel serviceModel = mapper.map(serviceRequest, ServiceModel.class);
        serviceModel = mapShop(shopModel, serviceModel);

        try {
            serviceModel = serviceRepository.save(serviceModel);
            return ResponseBuilder.okResponse(
                    "Tạo dịch vụ thành công",
                    mapper.map(serviceModel, ServiceResponse.class),
                    StatusCodeEnum.SERVICE1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    "Lưu dịch vụ thất bại",
                    StatusCodeEnum.SERVICE1001
            );
        }

    }

    public ResponseEntity<ResponseDto<ServiceResponse>> updateService(ServiceRequest serviceRequest, String id) {
        String idUser = userService.userId();
        ShopModel shopModel = shopRepository.findByIdUser(idUser);
        if (shopModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Cửa hàng không tồn tại",
                    StatusCodeEnum.SHOP1003
            );
        }
        ServiceModel serviceModel = serviceRepository.findById(id).orElse(null);
        mapper.maptoObject(serviceRequest, serviceModel);
        try {
            serviceModel = serviceRepository.save(serviceModel);
            return ResponseBuilder.okResponse(
                    "Cập nhật dịch vụ thành công",
                    mapper.map(serviceModel, ServiceResponse.class),
                    StatusCodeEnum.SERVICE1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    "Lưu dịch vụ thất bại",
                    StatusCodeEnum.SERVICE1001
            );
        }
    }

    public ResponseEntity<ResponseDto<ServiceResponse>> deleteService(String id) {
        ServiceModel serviceModel = serviceRepository.findById(id).orElse(null);
        if (serviceModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Dịch vụ không tồn tại",
                    StatusCodeEnum.SERVICE1001
            );
        }

        serviceModel.setDelete(true);
        try {
            serviceModel = serviceRepository.save(serviceModel);
            return ResponseBuilder.okResponse(
                    "Xóa thành công dịch vụ",
                    mapper.map(serviceModel, ServiceResponse.class),
                    StatusCodeEnum.SERVICE1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    "Xóa không thành công",
                    StatusCodeEnum.SERVICE1001
            );
        }
    }

    public ResponseEntity<ResponseDto<ServiceResponse>> getServiceById(String id) {
        ServiceModel serviceModel = serviceRepository.findByIdAndIsDelete(id, false);
        if (serviceModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Dịch vụ không tồn tại",
                    StatusCodeEnum.SERVICE1001
            );
        }

        return ResponseBuilder.okResponse(
                "Lấy dịch vụ của cửa hàng theo id thành công",
                mapper.map(serviceModel, ServiceResponse.class),
                StatusCodeEnum.SERVICE1000
        );
    }

    public ResponseEntity<ResponseDto<List<ServiceResponse>>> getAllService(int limit, int page, String s, String q, String filter) {
        Sort sort = Sort.by(Sort.Direction.DESC, s);
        Pageable pageable = PageRequest.of(page - 1, limit, sort);

        if (!Util.isNullOrEmpty(q) && !Util.isNullOrEmpty(filter)) {
            JSONObject jsonObject = new JSONObject(filter);
            Page<ServiceModel> serviceModelPage = serviceRepository.findByNameAndTypeAndIsDelete(q, jsonObject.get("type").toString(), false, pageable);
            List<ServiceModel> serviceModels = serviceModelPage.getContent();
            List<ServiceResponse> serviceResponses = serviceModels.stream()
                    .map(serviceModel -> mapper.map(serviceModel, ServiceResponse.class)).collect(Collectors.toList());

            MetaData metaData = MetaData.builder()
                    .totalPage(serviceModelPage.getTotalPages())
                    .currentPage(page)
                    .total(serviceModelPage.getTotalElements())
                    .pageSize(limit)
                    .build();

            return ResponseBuilder.okResponse(
                    "Lấy thành công danh sách dịch vụ của cửa hàng đó với từ khóa và lọc",
                    serviceResponses,
                    metaData,
                    StatusCodeEnum.SERVICE1002
            );
        }

        if (!Util.isNullOrEmpty(q)) {
            Page<ServiceModel> serviceModelPage = serviceRepository.findByNameContainingAndIsDelete(q, false, pageable);
            List<ServiceModel> serviceModels = serviceModelPage.getContent();
            List<ServiceResponse> serviceResponses = serviceModels.stream()
                    .map(serviceModel -> mapper.map(serviceModel, ServiceResponse.class)).collect(Collectors.toList());

            MetaData metaData = MetaData.builder()
                    .totalPage(serviceModelPage.getTotalPages())
                    .currentPage(page)
                    .total(serviceModelPage.getTotalElements())
                    .pageSize(limit)
                    .build();

            return ResponseBuilder.okResponse(
                    "Lấy thành công danh sách dịch vụ của cửa hàng đó với từ khóa",
                    serviceResponses,
                    metaData,
                    StatusCodeEnum.SERVICE1002
            );
        }

        if (!Util.isNullOrEmpty(filter)) {
            JSONObject jsonObject = new JSONObject(filter);
            Page<ServiceModel> serviceModelPage = serviceRepository.findByTypeAndIsDelete(jsonObject.get("type").toString(), false, pageable);
            List<ServiceModel> serviceModels = serviceModelPage.getContent();
            List<ServiceResponse> serviceResponses = serviceModels.stream()
                    .map(serviceModel -> mapper.map(serviceModel, ServiceResponse.class)).collect(Collectors.toList());

            MetaData metaData = MetaData.builder()
                    .totalPage(serviceModelPage.getTotalPages())
                    .currentPage(page)
                    .total(serviceModelPage.getTotalElements())
                    .pageSize(limit)
                    .build();

            return ResponseBuilder.okResponse(
                    "Lấy thành công danh sách dịch vụ của cửa hàng đó với lọc",
                    serviceResponses,
                    metaData,
                    StatusCodeEnum.SERVICE1002
            );
        }

        Page<ServiceModel> serviceModels = serviceRepository.findAllByIsDelete(false, pageable);
        List<ServiceModel> serviceModels1 = serviceModels.getContent();
        List<ServiceResponse> categoryResponseList = serviceModels1.stream().map(
                serviceModel -> mapper.map(serviceModel, ServiceResponse.class)
        ).collect(Collectors.toList());

        MetaData metaData = MetaData.builder()
                .totalPage(serviceModels.getTotalPages())
                .currentPage(page)
                .total(serviceModels.getTotalElements())
                .pageSize(limit)
                .build();
        return ResponseBuilder.okResponse(
                "Lấy thành công danh sách dịch vụ cửa hàng đó",
                categoryResponseList,
                metaData,
                StatusCodeEnum.SERVICE1002
        );
    }

    public ResponseEntity<ResponseDto<ShopResponse>> blockShop(String idShop) {
        ShopModel shopModel = shopRepository.findById(idShop).orElse(null);
        if (shopModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy cửa hàng",
                    StatusCodeEnum.SHOP1003
            );
        }

        shopModel.setVery(false);
        shopModel.setStatusShopEnums(StatusShopEnums.DEACTIVE);
        try {
            shopModel = shopRepository.save(shopModel);
            return ResponseBuilder.okResponse(
                    "Khóa cửa hàng thành công",
                    mapper.map(shopModel, ShopResponse.class),
                    StatusCodeEnum.SHOP1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    "Xảy ra lỗi khi lưu cửa hàng",
                    StatusCodeEnum.SHOP1001
            );
        }
    }

    public ResponseEntity<ResponseDto<Double>> getPointReview(String idShop) {
        ShopModel shopModel = shopRepository.findById(idShop).orElse(null);
        if (shopModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Cửa hàng không tồn tại",
                    StatusCodeEnum.SHOP1003
            );
        }

        List<ReviewModel> reviewModels = reviewRepository.findAllByIdShop(idShop);
        Double scoreReview = reviewModels.stream().mapToDouble(ReviewModel::getRating).average().orElse(0.0);
        return ResponseBuilder.okResponse(
                "Lấy trung bình điểm số review của cửa hàng thành công",
                scoreReview,
                StatusCodeEnum.REVIEW1000
        );

    }

    public ResponseEntity<ResponseDto<List<ShopResponse>>> getListShopDeActive(ShopDeactiveRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), Sort.by("createdAt").descending());

        String status = (request.getStatusShopEnums() == null || request.getStatusShopEnums().equals("")) ? "" :
                request.getStatusShopEnums().equals("ACTIVE") ?
                        StatusShopEnums.ACTIVE.toString() :
                        StatusShopEnums.DEACTIVE.toString();

        String keyword = request.getKeyword() != null ? request.getKeyword() : "";
        Page<ShopModel> shopModelList = shopRepository.findShopsByCriteria(keyword, status, pageable);
        List<ShopModel> shopModels = shopModelList.getContent();
        List<ShopResponse> shopResponses = shopModels.stream().map(
                shopModel -> mapper.map(shopModel, ShopResponse.class)
        ).collect(Collectors.toList());

        MetaData metaData = MetaData.builder()
                .currentPage(request.getPage())
                .pageSize(request.getSize())
                .total(shopModelList.getTotalElements())
                .totalPage(shopModelList.getTotalPages())
                .build();

        return ResponseBuilder.okResponse(
                "Lấy danh sách cửa hàng thành công",
                shopResponses,
                metaData,
                StatusCodeEnum.SHOP1000
        );
    }

    public ResponseEntity<ResponseDto<CountResponse>> getTotalShop(ShopTotalRequest request) {
        int total = shopRepository.countByCreatedAtBetween(request.getStartDate(), request.getEndDate());
        return ResponseBuilder.okResponse(
                "Tổng số cửa hàng theo thời gian",
                CountResponse.builder()
                        .total(total)
                        .build(),
                StatusCodeEnum.SHOP1000
        );
    }

    public ResponseEntity<ResponseDto<List<ReviewResponse>>> getReviewByShop(PanigationRequest request) {
        log.info("getReviewByShop() - request={}", request);
        String idUser = userService.userId();
        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit(), Sort.by("createdAt").descending());
        ShopModel shopModel = shopRepository.findByIdUser(idUser);
        if (shopModel == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy cửa hàng hiện tại",
                    StatusCodeEnum.SHOP1003
            );
        }

        Page<ReviewModel> reviewModelPage = reviewRepository.findAllByIdShop(shopModel.getId(), pageable);
        List<ReviewModel> reviewModels = reviewModelPage.getContent();
        List<ReviewResponse> reviewResponses = reviewModels.stream()
                .map(
                        reviewModel -> mapper.map(reviewModel, ReviewResponse.class)
                ).collect(Collectors.toList());
        MetaData metaData = MetaData.builder()
                .total(reviewModelPage.getTotalPages())
                .totalPage(reviewModelPage.getTotalPages())
                .currentPage(request.getPage())
                .pageSize(request.getLimit())
                .build();
        return ResponseBuilder.okResponse(
                "Lấy danh sách đánh giá theo cửa hàng thành công",
                reviewResponses,
                metaData,
                StatusCodeEnum.SHOP1000
        );
    }

    /**
     * Get list service
     * @Param panigationRequest
     * @return list ServiceResponse
     * */

    public ResponseEntity<ResponseDto<List<ServiceResponse>>> getListService(PanigationRequest panigationRequest){
        String idUser = userService.userId();
        ShopModel shopModel = shopRepository.findByIdUser(idUser);
        if(shopModel == null){
            return ResponseBuilder.badRequestResponse(
                    "Không tồn tại cửa hàng",
                    StatusCodeEnum.SHOP1003
            );
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "createAt");
        if (panigationRequest.getSort() != null){
            String sortField = panigationRequest.getSort().replace("-", "");
            Sort.Direction direction = panigationRequest.getSort().startsWith("-") ? Sort.Direction.DESC : Sort.Direction.ASC;
            sort = Sort.by(direction, sortField);
        }
        Pageable pageable = PageRequest.of(panigationRequest.getPage(), panigationRequest.getLimit(),sort);
        Criteria criteria = Criteria.where("idShop").is(shopModel.getId());

        if (panigationRequest.getKeyword() != null && !panigationRequest.getKeyword().trim().isEmpty()) {
            criteria = criteria.and("name").regex(panigationRequest.getKeyword(), "i");
        }
        criteria.and("isDelete").is(true);
        Query query = new Query(criteria).with(pageable);
        List<ServiceModel> services = mongoTemplate.find(query, ServiceModel.class);
        long totalElements = mongoTemplate.count(query.skip(0).limit(0), ServiceModel.class);

        List<ServiceResponse> serviceResponses = services.stream()
                .map(serviceModel ->  mapper.map(serviceModel, ServiceResponse.class)).collect(Collectors.toList());

        MetaData metaData = MetaData.builder()
                .total(totalElements)  // Tổng số bản ghi từ MongoDB
                .totalPage((int) Math.ceil((double) totalElements / panigationRequest.getLimit()))
                .currentPage(panigationRequest.getPage())
                .pageSize(panigationRequest.getLimit())
                .build();

        return ResponseBuilder.okResponse(
                "Lấy danh sách dịch vụ theo cửa hàng thành công",
                serviceResponses,
                metaData,
                StatusCodeEnum.SERVICE1000
        );
    }

    private ServiceModel mapShop(ShopModel shopModel, ServiceModel serviceModel) {
        serviceModel.setIdShop(shopModel.getId());
        serviceModel.setLatitude(shopModel.getLatitude());
        serviceModel.setLongitude(shopModel.getLongitude());
        serviceModel.setCity(shopModel.getCity());
        serviceModel.setWard(shopModel.getWard());
        serviceModel.setDistrict(shopModel.getDistrict());
        serviceModel.setIdCategory(shopModel.getIdCategory());
        serviceModel.setStateService(StateServiceEnums.OPEN);
        return serviceModel;
    }
}
