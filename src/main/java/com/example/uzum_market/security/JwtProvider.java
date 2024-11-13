package com.example.uzum_market.security;

import com.example.uzum_market.entity.User;
import com.example.uzum_market.repository.UserRepository;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class JwtProvider {

    @Autowired
    UserRepository userRepository;
    @Value("${spring.jwt.secret.key}")
    private String secretKey;
    @Value("${spring.expired.time}000")
    private Long expireDateMilliSecund;

    public String generateToken(User user) {
        Date issueDate = new Date();
        Date expireDate = new Date(issueDate.getTime() + expireDateMilliSecund);
        return Jwts
                .builder()
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(issueDate)
                .setExpiration(expireDate)
                .claim("username", user.getUsername())
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

//    public String generateToken(User user) {
//        Date issueDate = new Date();
//        Date expireDate = new Date(issueDate.getTime() + expireDateMiliSecund);
//        return Jwts
//                .builder()
//                .setSubject(user.getId().toString())
//                .setIssuedAt(issueDate)
//                .claim("id", String.valueOf(user.getId()))
//                .claim("username", user.getUsername())
//                .setExpiration(expireDate)
//                .signWith(SignatureAlgorithm.HS512, key)
//                .compact();
//    }


    public boolean valideToken(String token) {
        try {
            Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return true;
        } catch (
                ExpiredJwtException e) {
            System.err.println("Muttdati o'tgan");
        } catch (
                MalformedJwtException malformedJwtException) {
            System.err.println("Buzilgan token");
        } catch (
                SignatureException s) {
            System.err.println("Kalit so'z xato");
        } catch (
                UnsupportedJwtException unsupportedJwtException) {
            System.err.println("Qo'llanilmagan token");
        } catch (IllegalArgumentException ex) {
            System.err.println("Bo'sh token");
        }
        return false;
    }

    public User getUserFromToken(String token) {
        String userId = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();


        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
        return userOptional.orElse(null);

    }


}
