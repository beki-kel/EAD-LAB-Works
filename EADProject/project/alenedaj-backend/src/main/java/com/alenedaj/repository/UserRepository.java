package com.alenedaj.repository;

import com.alenedaj.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {  // ✅ Use ObjectId
    Optional<User> findByEmail(String email);
}
