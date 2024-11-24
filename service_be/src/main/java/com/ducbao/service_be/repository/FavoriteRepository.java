package com.ducbao.service_be.repository;

import com.ducbao.service_be.model.entity.FavoriteModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends MongoRepository<FavoriteModel, String> {
    boolean existsByIdUserAndIdShop(String idUser, String idShop);
    FavoriteModel findByIdUserAndIdShop(String idUser, String idShop);
    Page<FavoriteModel> findAllByIdUser(String idUser, Pageable pageable);
}
