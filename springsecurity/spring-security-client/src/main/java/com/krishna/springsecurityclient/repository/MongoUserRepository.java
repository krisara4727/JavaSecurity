package com.krishna.springsecurityclient.repository;

import com.krishna.springsecurityclient.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MongoUserRepository extends MongoRepository<User,Long> {
    User findByEmail(String email);
}
