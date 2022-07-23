package com.krishna.springsecurityclient.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

//@Entity
@Data
@NoArgsConstructor
@Document(collection = "verificationToken")
public class VerificationToken {
    private static final int EXPIRATION_TIME = 10;
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//
//    private Long id;
    @Id
    private String id;

    private String token;
    private Date expirationTime;
//    @OneToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "user_id",
//            nullable = false,
//            foreignKey = @ForeignKey(name = "FK_USER_VERIFY_TOKEN"))
//    private User user;

    private User user;

    public VerificationToken(String token){
        super();
        this.token = token;
        this.expirationTime = calcuateExpirationDate(EXPIRATION_TIME);
    }
    public VerificationToken(User user, String token){
        this.user = user;
        this.token = token;
        this.expirationTime = calcuateExpirationDate(EXPIRATION_TIME);
    }

    private Date calcuateExpirationDate(int expirationTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, expirationTime);
        return new Date(calendar.getTime().getTime());
    }
}
