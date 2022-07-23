package com.krishna.springsecurityclient.repository;

import com.krishna.springsecurityclient.entity.PasswordResetToken;
import com.krishna.springsecurityclient.entity.VerificationToken;
import com.krishna.springsecurityclient.model.ResetPasswordModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PasswordResetRepository extends MongoRepository<PasswordResetToken, String> {
    PasswordResetToken findByToken(String token);
}
