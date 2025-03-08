package com.ducbao.service_be.repository;

import com.ducbao.service_be.model.entity.ADSSubscriptionModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ADSSubscriptionRepository extends MongoRepository<ADSSubscriptionModel, String> {
    Optional<ADSSubscriptionModel> findByVnpTxnRef(String vnp_TxnRef);
}
