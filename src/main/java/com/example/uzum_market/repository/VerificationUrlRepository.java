package com.example.uzum_market.repository;

import com.example.uzum_market.entity.VerificationUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Optional;

public interface
VerificationUrlRepository extends JpaRepository<VerificationUrl, Long> {
    Optional<VerificationUrl> findByToken(String token);


    Optional<VerificationUrl> findByTokenAndExpiryDateGreaterThan(String token, Timestamp expiryDate);

    @Query(nativeQuery = true, value = "SELECT *  FROM verification_url WHERE token = :email AND expiry_date > NOW()")
    Optional<VerificationUrl> findByEmail(@Param("email") String email);

    void deleteByEmail(String email);
}