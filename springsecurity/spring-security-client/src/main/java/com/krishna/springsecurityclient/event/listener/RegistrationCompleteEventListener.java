package com.krishna.springsecurityclient.event.listener;

import com.krishna.springsecurityclient.entity.User;
import com.krishna.springsecurityclient.event.RegistrationCompleteEvent;
import com.krishna.springsecurityclient.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import java.util.UUID;
@Slf4j

public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        User user = event.getUser();
        log.info("here is the user after registration " + user);
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token, user);

        String url = event.getApplicationUrl() + "verifyRegistration?token" + token;
        // send verification email from here.
        log.info("click on the link to verify registration " +  url);
    }
}
