package com.krishna.springsecurityclient.service;

import com.krishna.springsecurityclient.entity.User;
import com.krishna.springsecurityclient.entity.VerificationToken;
import com.krishna.springsecurityclient.model.ResetPasswordModel;
import com.krishna.springsecurityclient.model.UserModel;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface UserService {
    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(String token, User user);

    String validateVerificationToken(String token);

    VerificationToken generateNewVerificationToken(String oldToken);

    User findUserByEmail(String email);

    void createPasswordResetTokenForUser(User user, String token);

    String validateResetPasswordToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    void saveUserNewPassword(User user, String newPassword);
}
