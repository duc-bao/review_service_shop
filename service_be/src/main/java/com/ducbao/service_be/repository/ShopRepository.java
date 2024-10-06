package com.ducbao.service_be.repository;

import com.ducbao.service_be.model.entity.ShopModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends MongoRepository<ShopModel, String> {
}
