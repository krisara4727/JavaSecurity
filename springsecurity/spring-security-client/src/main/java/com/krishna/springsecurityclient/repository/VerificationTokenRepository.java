//package com.krishna.springsecurityclient.repository;
package com.krishna.springsecurityclient.repository;
//
import com.krishna.springsecurityclient.entity.VerificationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.stereotype.Repository;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
//}

public interface VerificationTokenRepository extends MongoRepository<VerificationToken, String> {

     VerificationToken findByToken(String token);
}