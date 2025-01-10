package com.ducbao.service_be.repository;

import com.ducbao.service_be.model.entity.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserModel, String> {
    Optional<UserModel> findByUsername(String username);
    Optional<UserModel> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    UserModel findByActiveCode(String activeCode);

    int countByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
}
