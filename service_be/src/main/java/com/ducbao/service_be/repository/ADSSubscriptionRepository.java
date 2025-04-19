package com.ducbao.service_be.repository;

import com.ducbao.service_be.model.entity.ADSSubscriptionModel;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ADSSubscriptionRepository extends MongoRepository<ADSSubscriptionModel, String> {
    Optional<ADSSubscriptionModel> findByVnpTxnRef(String vnp_TxnRef);

    List<ADSSubscriptionModel> findAllByIdShop(String idShop);
    Optional<ADSSubscriptionModel> findByIdShop(String idShop);

    Integer countAllByIssuedAtAfterAndExpiredAtBefore(LocalDateTime issuedAt,LocalDateTime expiredAt);
    Integer countAllByIdShop(String idShop);
    @Aggregation(pipeline = {
            "{ $match: { idShop: ?0 } }",
            "{ $group: { _id: null, totalView: { $sum: \"$totalView\" } } }"
    })
    Integer sumTotalViewByIdShop(String idShop);

    boolean existsByIdShopAndAndIdAdvertisement(String idShop, String idAdvertisement);
}
