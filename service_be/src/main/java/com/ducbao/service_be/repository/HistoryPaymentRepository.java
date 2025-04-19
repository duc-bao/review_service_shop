package com.ducbao.service_be.repository;

import com.ducbao.service_be.model.entity.HistoryPaymentModel;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryPaymentRepository extends MongoRepository<HistoryPaymentModel, String> {
    @Aggregation(pipeline = {
            "{ $group: { _id: null, total: { $sum: \"$totalAmount\" } } }"
    })
    Double sumTotalAmount();
}
