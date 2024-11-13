package com.example.uzum_market.repository;

import com.example.uzum_market.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);


    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);


    Optional<User> deleteUserById(Long id);
}
