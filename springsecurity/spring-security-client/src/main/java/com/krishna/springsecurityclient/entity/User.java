package com.krishna.springsecurityclient.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;


//@Entity
@Data
@Document(collection = "todoList")
public class User {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String id;
    private String firstname;
    private String lastname;
    private String email;
//    @Column(length = 60)
    private String password;
    private String role;
    private boolean enabled = false;
}
