package com.example.uzum_market.repository;

import com.example.uzum_market.entity.Cart;
import com.example.uzum_market.entity.Product;
import com.example.uzum_market.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE cart c SET c.count = c.count + 1 WHERE c.product_id = :productId AND c.user_id = :currentUserId")
    void incrementProductCount(@Param("productId") Long id, @Param("currentUserId") Long currentUserId);

    void deleteByIdAndUser(Long id, User user);

    Optional<Cart> findByUserIdAndProduct(Long user_id, Product product);

    @Query(nativeQuery = true, value = "SELECT * from cart where user_id = (SELECT id from users LIMIT 1);")
    List<Cart> findAllMyCartNative(Long id);
}
