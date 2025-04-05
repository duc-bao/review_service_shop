package com.ducbao.service_be.repository;

import com.ducbao.service_be.model.entity.ServiceModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends MongoRepository<ServiceModel, String> {
    ServiceModel findByIdAndIsDelete(String id, boolean isDelete);
    Page<ServiceModel> findByNameAndTypeAndIsDelete(String n, String t, boolean isDelete, Pageable pageable);
    Page<ServiceModel> findByNameContainingAndIsDelete(String n,boolean isDelete ,Pageable pageable);
    Page<ServiceModel> findByTypeAndIsDelete(String t, boolean isDelete, Pageable pageable);
    Page<ServiceModel> findAllByIsDelete(boolean isDelete, Pageable pageable);
    List<ServiceModel> findAllByIdShop(String idShop);
    Page<ServiceModel> findAllByIdShopAndIsDelete(String idShop,boolean isDelete, Pageable pageable);

    @Query("{ 'idShop': ?0, " +
            "?#{ [1] != null ? {'name': { $regex: [1], $options: 'i' }} : {}} " +
            "}")
    Page<ServiceModel> findByIdShopAndKeyword(String idShop, String keyword, Pageable pageable);


}
