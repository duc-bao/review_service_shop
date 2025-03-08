package com.ducbao.service_be.controller;

import com.ducbao.common.model.enums.StatusAdvertisement;
import com.ducbao.service_be.model.entity.ADSSubscriptionModel;
import com.ducbao.service_be.model.entity.AdvertisementModel;
import com.ducbao.service_be.model.entity.HistoryPaymentModel;
import com.ducbao.service_be.repository.ADSSubscriptionRepository;
import com.ducbao.common.model.enums.StatusPaymentEnum;
import com.ducbao.service_be.repository.AdvertisementRepository;
import com.ducbao.service_be.repository.HistoryPaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class WebhookController {
    private final ADSSubscriptionRepository adsSubscriptionRepository;
    private final AdvertisementRepository advertisementRepository;
    private final HistoryPaymentRepository historyPaymentRepository;

    @GetMapping("/IPN")
    public ResponseEntity<String> handleIPN(
            @RequestParam("vnp_TxnRef") String transactionId,
            @RequestParam("vnp_ResponseCode") String responseCode,
            @RequestParam("vnp_TransactionStatus") String transactionStatus,
            @RequestParam("vnp_SecureHash") String secureHash,
            @RequestParam("vnp_PayDate") String vnp_PayDate,
            @RequestParam("vnp_OrderInfo") String vnp_OrderInfo,
            @RequestParam("vnp_TransactionStatus") String vnp_TransactionStatus,
            @RequestParam("vnp_BankCode") String vnp_BankCode,
            @RequestParam("vnp_TmnCode") String vnp_TmnCode,
            @RequestParam("vnp_TransactionNo") String vnp_TransactionNo
    ) {
        log.info("Received IPN request from VNPAY: Transaction ID = {}, Response Code = {}, Status = {}",
                transactionId, responseCode, transactionStatus);

        // Kiểm tra tính hợp lệ của chữ ký (SecureHash) nếu cần
        // TODO: Thực hiện xác minh secureHash với khóa bí mật của VNPAY để tránh giả mạo

        // Kiểm tra giao dịch có hợp lệ không
        Optional<ADSSubscriptionModel> optionalSubscription = adsSubscriptionRepository.findByVnpTxnRef(transactionId);
        if (optionalSubscription.isEmpty()) {
            log.error("Transaction ID {} not found!", transactionId);
            return ResponseEntity.badRequest().body("Transaction not found");
        }
        ADSSubscriptionModel adsSubscription = optionalSubscription.get();
        AdvertisementModel advertisementModel = advertisementRepository.findById(adsSubscription.getIdAdvertisement()).orElse(null);


        HistoryPaymentModel historyPaymentModel = HistoryPaymentModel.builder()
                .idAds(adsSubscription.getIdAdvertisement())
                .idShop(adsSubscription.getIdShop())
                .transactionId(transactionId)
                .vnp_TransactionNo(vnp_TransactionNo)
                .contentPayment(vnp_OrderInfo)
                .totalAmount(advertisementModel.getTotalAccess())
                .build();
        // Kiểm tra trạng thái giao dịch từ VNPAY
        if ("00".equals(responseCode) && "00".equals(transactionStatus)) {
            // Giao dịch thành công -> Cập nhật trạng thái thanh toán
            adsSubscription.setStatusPayment(StatusPaymentEnum.SUCCESS);
            adsSubscription.setStatus(StatusAdvertisement.OPEN);
            historyPaymentModel.setStatusPayment(StatusPaymentEnum.SUCCESS);
            adsSubscriptionRepository.save(adsSubscription);
            historyPaymentRepository.save(historyPaymentModel);
            log.info("Payment successful for transaction {}", transactionId);
            return ResponseEntity.ok("Transaction successful");
        } else {
            // Giao dịch thất bại -> Cập nhật trạng thái
            adsSubscription.setStatusPayment(StatusPaymentEnum.FAILURE);
            historyPaymentModel.setStatusPayment(StatusPaymentEnum.FAILURE);
            adsSubscriptionRepository.save(adsSubscription);
            historyPaymentRepository.save(historyPaymentModel);
            log.warn("Payment failed for transaction {}", transactionId);
            return ResponseEntity.ok("Transaction failed");
        }
    }
}
