package com.example.demo.repository;

import com.example.demo.model.EmailVerificationStatus;
import com.example.demo.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {
    Optional<User> findByUsername(String username);
    User findByEmailVerificationToken(String token);
    User findByEmailVerificationStatus(EmailVerificationStatus verificationStatus);
}
