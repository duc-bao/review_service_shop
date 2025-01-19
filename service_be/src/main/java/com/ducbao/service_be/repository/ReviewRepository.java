package com.ducbao.service_be.repository;

import com.ducbao.service_be.model.entity.ReviewModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<ReviewModel, String> {
    Page<ReviewModel> findByIdShop(String idShop, Pageable pageable);
    Page<ReviewModel> findByIdService(String idService, Pageable pageable);
    Page<ReviewModel> findByIdUser(String idUser, Pageable pageable);
    List<ReviewModel> findAllByIdShop(String idShop);
    Page<ReviewModel> findAllByIdShop(String idUser, Pageable pageable);
    int countByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    int countByCreatedAtBetweenAndIdShop(LocalDateTime from, LocalDateTime to, String idShop);
}
