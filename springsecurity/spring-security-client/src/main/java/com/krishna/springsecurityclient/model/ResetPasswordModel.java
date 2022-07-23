package com.krishna.springsecurityclient.model;

import lombok.Data;

@Data
public class ResetPasswordModel {
    private String email;
    private String oldPassword;
    private String newPassword;
}
