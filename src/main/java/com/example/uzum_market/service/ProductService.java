package com.example.uzum_market.service;

import com.example.uzum_market.entity.Category;
import com.example.uzum_market.entity.Product;
import com.example.uzum_market.entity.User;
import com.example.uzum_market.payload.dto.ProductDto;
import com.example.uzum_market.payload.projection.ProductProjection;
import com.example.uzum_market.repository.CartRepository;
import com.example.uzum_market.repository.CategoryRepository;
import com.example.uzum_market.repository.ProductRepository;
import com.example.uzum_market.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {


    protected Path root = Paths.get("/com/example/uzum_market/service/photo folder");
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CartRepository cartRepository;

    public HttpEntity<?> createNewProduct(ProductDto productDto, User currentUser) {

        //time of now
        Timestamp timestamp;


        Product product = new Product();
        try {
            Optional<User> byUserName = userRepository.findByUserName(currentUser.getUsername());
            if (byUserName.isEmpty()) {
                return ResponseEntity.status(404).body("User Not found");
            }

            User user = byUserName.get();
            String username = user.getUsername();
            final Optional<Category> byId = categoryRepository.findById(productDto.getCategory().getId());
            if (byId.isPresent()) {
                try {
                    Files.createDirectories(root);
                } catch (IOException e) {
                    throw new RuntimeException("Could not initialize folder for upload!");
                }
                product.setProductName(productDto.getProductName());
                product.setDescription(productDto.getDescription());
                if (productDto.getImage() != null) {
                    String filename = UUID.randomUUID() + "_" + productDto.getImage().getOriginalFilename();
                    Files.copy(productDto.getImage().getInputStream(), this.root.resolve(filename));
                    // Fayl yo'lini saqlaymiz
                    product.setPathPhoto(filename);
                }
                product.setCreatedBy(user.getId());
                product.setCategory(productDto.getCategory());
                productRepository.save(product);
            }
            return ResponseEntity.status(201).body("Product Ctreared");
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e + " Product don't created");
        }

    }


    public HttpEntity<?> myProductList(User currentUser) {
        List<Product> allByCreatedBy = productRepository.findAllByCreatedBy(currentUser.getId());
        return ResponseEntity.status(201).body(allByCreatedBy);
    }

    public HttpEntity<?> getById(Long id) {
        try {
            Optional<ProductProjection> productById = productRepository.findByIdNative(id);
            if (productById.isPresent()) {
                return ResponseEntity.status(200).body(productById.get());
            }
        } catch (Exception e) {
            ResponseEntity.status(403).body(e);
        }
        return ResponseEntity.status(404).body("No Product");
    }

    public HttpEntity<?> editProduct(Long id, ProductDto productDto, User user) {
        try {
            Optional<Product> productId = productRepository.findById(id);
            Product getProductId = productId.get();

            if (productId.isEmpty() && getProductId.getCreatedBy().equals(user.getId())) {
                Product product = new Product();
                product.setProductName(productDto.getProductName());
                product.setPathPhoto(productDto.getPathPhoto());
                product.setPrice(product.getPrice());
                product.setDescription(productDto.getDescription());
                product.setUpdatedBy(user.getId());
                productRepository.save(product);
            }
        } catch (Exception e) {

        }
        return null;
    }


    public HttpEntity<?> deleteProduct(Long id, User user) {
        try {
            Optional<Product> productId = productRepository.findById(id);
            Product getProductId = productId.get();
            if (!getProductId.isDeleted() && getProductId.getCreatedBy().equals(user.getId())) {
                getProductId.setDeleted(true);
                productRepository.save(getProductId);
                return ResponseEntity.status(201).body("Product successfully deleted");
            }
            return ResponseEntity.status(400).body("Product is not deleted ");

        } catch (Exception e) {
            return ResponseEntity.status(500).body(e);
        }
    }


    public HttpEntity<?> allProductList() {
        try {
            List<ProductProjection> products = productRepository.findAllNative();

            return ResponseEntity.status(200).body(products);

        } catch (Exception e) {

            return ResponseEntity.status(400).body(e);
        }

    }


}

