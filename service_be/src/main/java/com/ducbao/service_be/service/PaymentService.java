package com.ducbao.service_be.service;

import com.ducbao.common.model.builder.ResponseBuilder;
import com.ducbao.common.model.dto.MetaData;
import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.common.model.enums.StatusAdvertisement;
import com.ducbao.common.model.enums.StatusCodeEnum;
import com.ducbao.common.model.enums.StatusPaymentEnum;
import com.ducbao.service_be.config.payment.VNPayConfig;
import com.ducbao.service_be.model.dto.request.ADSShopRequest;
import com.ducbao.service_be.model.dto.request.PanigationRequest;
import com.ducbao.service_be.model.dto.response.HistoryPaymentResponse;
import com.ducbao.service_be.model.entity.ADSSubscriptionModel;
import com.ducbao.service_be.model.entity.AdvertisementModel;
import com.ducbao.service_be.model.entity.HistoryPaymentModel;
import com.ducbao.service_be.model.entity.ShopModel;
import com.ducbao.service_be.model.mapper.CommonMapper;
import com.ducbao.service_be.repository.ADSSubscriptionRepository;
import com.ducbao.service_be.repository.AdvertisementRepository;
import com.ducbao.service_be.repository.ShopRepository;
import com.ducbao.service_be.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final VNPayConfig vnpayConfig;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final AdvertisementRepository advertisementRepository;
    private final ADSSubscriptionRepository adsSubscriptionRepository;
    private final CommonMapper mapper;
    private final MongoTemplate mongoTemplate;

    public ResponseEntity<?> createPayment(ADSShopRequest adsShopRequest)
            throws  UnsupportedEncodingException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String orderType = "other";
        long amount = (long) (adsShopRequest.getAmount() * 100);
        String vnp_TxnRef = vnpayConfig.getRandomNumber(8);
        String vnp_IpAddr = vnpayConfig.getIpAddress(request);
        String vnp_TmnCode = vnpayConfig.vnp_TmnCode;
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnpayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", vnpayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;
        // Get Info Shop
        String idUser = userService.userId();
        ShopModel shopModel = shopRepository.findByIdUser(idUser);
        if(shopModel == null){
            return ResponseBuilder.badRequestResponse(
                    "Cửa hàng không tồn tại",
                    StatusCodeEnum.SHOP1003
            );
        }

        AdvertisementModel advertisementModel = advertisementRepository.findById(adsShopRequest.getIdAdvertisement()).orElse(null);
        if(advertisementModel == null){
            return ResponseBuilder.badRequestResponse(
                    "Gói quảng cáo không tồn tại",
                    StatusCodeEnum.ADVERTISEMENT0404
            );
        }

        if (advertisementModel.getStatusAdvertisement().equals(StatusAdvertisement.CLOSE)) {
            return ResponseBuilder.badRequestResponse(
                    "Không thể đăng ký gói quảng cáo này vì đã bị đóng",
                    StatusCodeEnum.ADVERTISEMENT0101
            );
        }

        ADSSubscriptionModel adsSubscriptionModel = ADSSubscriptionModel.builder()
                .statusPayment(StatusPaymentEnum.PENDING)
                .idAdvertisement(advertisementModel.getId())
                .idShop(shopModel.getId())
                .issuedAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusDays(advertisementModel.getDuration().getDayOfMonth()))
                .status(StatusAdvertisement.CLOSE)
                .vnpTxnRef(vnp_TxnRef)
                .build();

        try {
            adsSubscriptionRepository.save(adsSubscriptionModel);
            return ResponseEntity.status(HttpStatus.OK).body(paymentUrl);
        }catch (Exception e){
            log.error("Error createPayement() - {}", e.getMessage());
            return ResponseBuilder.badRequestResponse(
                    "Lỗi khi tạo thanh toán",
                    StatusCodeEnum.ADVERTISEMENT0101
            );
        }
    }

    public ResponseEntity<ResponseDto<List<HistoryPaymentResponse>>> getVnPayInfo(PanigationRequest request) {
        String idUser = userService.userId();
        ShopModel shopModel = shopRepository.findByIdUser(idUser);
        if(shopModel == null){
            return ResponseBuilder.badRequestResponse(
                    "Cửa hàng không tồn tại",
                    StatusCodeEnum.SHOP1003
            );
        }

        Sort sort = Sort.by(Sort.Direction.ASC, "createAt");
        if(request.getSort() != null){
            String field = request.getSort().replace("-", "");
            Sort.Direction sortDirection = request.getSort().startsWith("-") ? Sort.Direction.DESC : Sort.Direction.ASC;
            sort = Sort.by(sortDirection, field);
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit(), sort);

        org.springframework.data.mongodb.core.query.Criteria criteria = new Criteria();
        criteria.where("idShop").is(shopModel.getId());
        if(request.getKeyword() != null){
            criteria.and("transactionId").is(request.getKeyword());
        }
        Query query = new Query(criteria).with(pageable);
        List<HistoryPaymentModel> historyPaymentModels = mongoTemplate.find(query, HistoryPaymentModel.class);
        List<HistoryPaymentResponse> historyPaymentResponses = historyPaymentModels.stream()
                .map(historyPaymentModel -> mapper.map(historyPaymentModel,HistoryPaymentResponse.class)).collect(Collectors.toList());
        long total = mongoTemplate.count(query.skip(0).limit(0), HistoryPaymentResponse.class);
        MetaData metaData = MetaData.builder()
                .pageSize(request.getLimit())
                .total(total)
                .currentPage(request.getPage())
                .totalPage((int) Math.ceil((double) total) / request.getLimit())
                .build();

        return ResponseBuilder.okResponse(
                "Lấy danh sách lịch sử hóa đơn thành công",
                historyPaymentResponses,
                metaData,
                StatusCodeEnum.ADVERTISEMENT1000
        );
    }
}
