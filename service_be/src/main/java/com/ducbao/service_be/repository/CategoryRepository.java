package com.ducbao.service_be.repository;

import com.ducbao.service_be.model.entity.CategoryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CategoryRepository extends MongoRepository<CategoryModel, String> {
    Page<CategoryModel> findByNameContainingAndIsDelete(String categoryName,boolean isDelete, Pageable pageable);
    Page<CategoryModel> findByTypeAndIsDelete(String type,boolean isDelete ,Pageable pageable);
    Page<CategoryModel> findByNameContainingAndTypeAndIsDelete(String categoryName, String type, boolean isDelete,Pageable pageable);
    Page<CategoryModel> findAllByIsDelete(boolean isDelete, Pageable pageable);
    CategoryModel findByIdAndIsDelete(String id,boolean isDelete);
    boolean existsByName(String name);

    int countByCreatedAtBetweenAndIsDeleteIsFalse(LocalDateTime from, LocalDateTime to);
    List<CategoryModel> findAllByParentIdIsNull();
}
