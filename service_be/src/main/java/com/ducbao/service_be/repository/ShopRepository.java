package com.ducbao.service_be.repository;

import com.ducbao.common.model.enums.StatusShopEnums;
import com.ducbao.service_be.model.entity.ShopModel;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShopRepository extends MongoRepository<ShopModel, String> {
    ShopModel findByIdUser(String idUser);

    @Query(value = "{ $and: [ " +
            // Keyword search condition
            "{ $or: [" +
            "  { 'name': { $regex: ?0, $options: 'i' } }," +
            "  { 'description': { $regex: ?0, $options: 'i' } }" +
            "] }," +
            // Status condition - if status is empty string, this condition will be true for all documents
            "{ $or: [" +
            "  { $expr: { $eq: ['', ?1] } }," +  // If status parameter is empty, match all
            "  { 'statusShopEnums': ?1 }" +       // Otherwise, match specific status
            "] }" +
            "] }")
    Page<ShopModel> findShopsByCriteria(
            @Param("keyword") String keyword,
            @Param("status") String status,
            Pageable pageable
    );


    int countByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
}
