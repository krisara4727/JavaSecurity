package com.krishna.springsecurityclient.service;

import com.krishna.springsecurityclient.entity.PasswordResetToken;
import com.krishna.springsecurityclient.entity.User;
import com.krishna.springsecurityclient.entity.VerificationToken;
import com.krishna.springsecurityclient.model.UserModel;
import com.krishna.springsecurityclient.repository.MongoUserRepository;
//import com.krishna.springsecurityclient.repository.UserRepository;
//import com.krishna.springsecurityclient.repository.VerificationTokenRepository;
import com.krishna.springsecurityclient.repository.PasswordResetRepository;
import com.krishna.springsecurityclient.repository.VerificationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    //@Autowired
    //private UserRepository userRepository;

    @Autowired
    private MongoUserRepository mongoUserRepository;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetRepository passwordResetRepository;

    @Override
    public User registerUser(UserModel userModel) {
        log.info("Register user *****" + userModel);
        User user = new User();
        user.setEmail(userModel.getEmail());
        user.setFirstname(userModel.getFirstname());
        user.setLastname(userModel.getLastname());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        log.info("user details" + user);
        mongoUserRepository.save(user);
        return user;
    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            return "invalid token";
        }
        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();

        if (verificationToken.getExpirationTime().getTime() - cal.getTime().getTime() <= 0) {
            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }
        user.setEnabled(true);
        mongoUserRepository.save(user);
        return "valid";
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    @Override
    public User findUserByEmail(String email) {
        return mongoUserRepository.findByEmail(email);
    }
    @Override
    public  void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(user, token);
        passwordResetRepository.save(passwordResetToken);
    }
    @Override
    public String validateResetPasswordToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetRepository.findByToken(token);
        if (passwordResetToken == null) {
            return "invalid token";
        }
        User user = passwordResetToken.getUser();
        Calendar cal = Calendar.getInstance();

        if (passwordResetToken.getExpirationTime().getTime() - cal.getTime().getTime() <= 0) {
            passwordResetRepository.delete(passwordResetToken);
            return "expired";
        }

        return "valid";
    }
    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetRepository.findByToken(token).getUser());
    }

    @Override
    public void saveUserNewPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        mongoUserRepository.save(user);
    }
}
