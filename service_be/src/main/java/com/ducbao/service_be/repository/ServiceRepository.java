package com.ducbao.service_be.repository;

import com.ducbao.service_be.model.entity.ServiceModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends MongoRepository<ServiceModel, String> {
}
