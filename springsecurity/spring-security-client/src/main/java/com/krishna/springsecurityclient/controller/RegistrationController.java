package com.krishna.springsecurityclient.controller;

import com.krishna.springsecurityclient.entity.User;
import com.krishna.springsecurityclient.entity.VerificationToken;
import com.krishna.springsecurityclient.event.RegistrationCompleteEvent;
import com.krishna.springsecurityclient.model.ResetPasswordModel;
import com.krishna.springsecurityclient.model.UserModel;
import com.krishna.springsecurityclient.repository.VerificationTokenRepository;
import com.krishna.springsecurityclient.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class RegistrationController {
    @Autowired
    private UserService userService;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private ApplicationEventPublisher publisher;



    @GetMapping("/hello")
    public String sayHello(){
        return "hello world ";
    }
    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request){
        log.info("Register user"+ userModel);
        User user = userService.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompleteEvent(user,applicationUrl(request)));
        String token = UUID.randomUUID().toString();
        String url = applicationUrl(request) + "/verifyRegistration?token=" + token;
        // String url = event.getApplicationUrl() + "verifyRegistration?token" + token;
        VerificationToken verificationToken = new VerificationToken(user, token);
        log.info("click the link " + url);
        verificationTokenRepository.save(verificationToken);
        return "success" + user;
    }

    @GetMapping("/resendVerifyToken")
    private String resendVerifyToken(@RequestParam("token") String oldToken, HttpServletRequest request) {
        VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken);
        User user = verificationToken.getUser();
        resendVerifyTokenMail(user, applicationUrl(request), verificationToken);
        return "verification link resent";
    }

    @GetMapping("/verifyRegistration")
    private String verifyRegistration(@RequestParam("token") String token) {
        String result = userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid")) {
            return "valid verification successfully";
        } else {
            return "bad User";
        }
    }
    private String applicationUrl(HttpServletRequest request) {
        return "http://"+ request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    @PostMapping("/resetPassword")
    private String resetPassword(@RequestBody  ResetPasswordModel resetpasswordModel, HttpServletRequest request) {
        User user = userService.findUserByEmail(resetpasswordModel.getEmail());
        String url = "";
        if( user != null){
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            url = passwordResetTokenModel(user, applicationUrl(request), token);
        }
        return url;
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token, @RequestBody ResetPasswordModel resetpasswordModel) {
        // find user from token and verify token with expiry
        String result = userService.validateResetPasswordToken(token);
        if(!result.equalsIgnoreCase("valid")) {
            return "Invalid token";
        }
        Optional<User> user = userService.getUserByPasswordResetToken(token);
        if(user.isPresent()){
            userService.saveUserNewPassword(user.get(), resetpasswordModel.getNewPassword());
            return "Password changed successfully";
        } else {
            return "Invalid token";
        }
    }


    public String passwordResetTokenModel(User user, String applicationUrl, String token) {

        String url = applicationUrl + "/savePassword?token=" + token;
        log.info("click on save password change link " + url);
        return url;
    }

    private void resendVerifyTokenMail(User user, String applicationUrl, VerificationToken verificationToken) {
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token, user);

        String url = applicationUrl + "/verifyRegistration?token=" + verificationToken.getToken();
        log.info("click on resent link " + url);
    }

}
