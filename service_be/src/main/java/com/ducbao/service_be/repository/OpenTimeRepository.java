package com.ducbao.service_be.repository;

import com.ducbao.service_be.model.entity.OpenTimeModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenTimeRepository extends MongoRepository<OpenTimeModel, String> {
}
