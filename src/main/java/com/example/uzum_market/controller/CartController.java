package com.example.uzum_market.controller;

import com.example.uzum_market.annotations.CurrentUser;
import com.example.uzum_market.entity.User;
import com.example.uzum_market.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {


    @Autowired
    private CartService cartService;

    @PostMapping("/add-cart/{id}")
    public HttpEntity<?> addCart(@PathVariable("id") Long id, @CurrentUser User user) {
        return cartService.addCart(id, user);
    }

    @DeleteMapping("/delete/{id}")
    public HttpEntity<?> deleteAtCart(@PathVariable("id") Long id, @CurrentUser User user) {
        return cartService.deleteAtCart(id, user);
    }

    @GetMapping("/my-cart")
    public HttpEntity<?> myCart(@CurrentUser User currentUser) {
        return cartService.myCart(currentUser);
    }
}
