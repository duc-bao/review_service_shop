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

    @Query(value = "{ $and: [ "
            + "{ $or: ["
            + "  { 'name': { $regex: ?0, $options: 'i' } },"
            + "  { 'description': { $regex: ?0, $options: 'i' } }"
            + "] },"
            + "{ 'statusShopEnums': ?1 }"
            + "] }")
    Page<ShopModel> findShopsByCriteria(
            @Param("keyword") String keyword,
            @Param("status") String status,
            Pageable pageable
    );

    int countByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
}
