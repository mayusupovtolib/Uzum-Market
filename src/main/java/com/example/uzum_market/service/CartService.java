package com.example.uzum_market.service;


import com.example.uzum_market.entity.Cart;
import com.example.uzum_market.entity.Product;
import com.example.uzum_market.entity.User;
import com.example.uzum_market.repository.CartRepository;
import com.example.uzum_market.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartRepository cartRepository;

    @Transactional
    public HttpEntity<?> addCart(Long productId, User currentUser) {
        try {
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isEmpty()) {
                return ResponseEntity.status(400).body("Product not found");
            }
            Product product = optionalProduct.get();

            Optional<Cart> optionalCart = cartRepository.findByUserIdAndProduct(currentUser.getId(), product);
            Cart cart;
            if (optionalCart.isEmpty()) {
                cart = new Cart();
                cart.setProduct(product);
                cart.setUser(currentUser);
            } else {
                cart = optionalCart.get();
                cart.setCount(cart.getCount() + 1);
            }
            cartRepository.save(cart);

            return ResponseEntity.status(200).body("ok");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    public HttpEntity<?> deleteAtCart(Long id, User currentUser) {
        try {

            cartRepository.deleteByIdAndUser(id, currentUser);
            return ResponseEntity.status(200).body("Product is deleted");
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    public HttpEntity<?> myCart(User currentUser) {
        try {
            List<Cart> allMyCart = cartRepository.findAllMyCartNative(currentUser.getId());
            if (allMyCart.isEmpty()) {
                return ResponseEntity.status(200).body("Savatingiz bo'sh");

            }
            return ResponseEntity.status(200).body(allMyCart);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Cartlarni qidirishda xatolik " + e);
        }
    }
}
