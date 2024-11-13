package com.example.uzum_market.repository;

import com.example.uzum_market.entity.Product;
import com.example.uzum_market.payload.projection.ProductProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByCreatedBy(Long id);

    @Query(nativeQuery = true, value = "SELECT  id,product_name as productName,path_photo as pathPhoto,description,category_id as categoryId FROM product where id = :id")
    Optional<ProductProjection> findByIdNative(@Param("id") Long id);

    @Query(nativeQuery = true, value = "SELECT id,product_name as productName,path_photo as pathPhoto,description,category_id as categoryId,price FROM product WHERE deleted = FALSE ")
    List<ProductProjection> findAllNative();
    
}
