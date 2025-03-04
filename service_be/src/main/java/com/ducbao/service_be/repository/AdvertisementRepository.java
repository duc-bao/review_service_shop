package com.ducbao.service_be.repository;

import com.ducbao.service_be.model.entity.AdvertisementModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertisementRepository extends MongoRepository<AdvertisementModel, String> {
    boolean existsByName(String name);
}
