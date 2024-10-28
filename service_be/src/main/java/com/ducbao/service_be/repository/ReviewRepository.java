package com.ducbao.service_be.repository;

import com.ducbao.service_be.model.entity.ReviewModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends MongoRepository<ReviewModel, String> {
    Page<ReviewModel> findByIdShop(String idShop, Pageable pageable);
    Page<ReviewModel> findByIdService(String idService, Pageable pageable);
    Page<ReviewModel> findByIdUser(String idUser, Pageable pageable);
}
