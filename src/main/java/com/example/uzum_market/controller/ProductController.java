package com.example.uzum_market.controller;

import com.example.uzum_market.annotations.CurrentUser;
import com.example.uzum_market.entity.User;
import com.example.uzum_market.payload.dto.ProductDto;
import com.example.uzum_market.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/all")
    public HttpEntity<?> allProduct() {
        return productService.allProductList();
    }


    @PostMapping("/new")
    public HttpEntity<?> createNewProduct(@RequestBody ProductDto productDto, @CurrentUser User currentUser) {
        return productService.createNewProduct(productDto, currentUser);

    }

    @GetMapping("/my-product")
    public HttpEntity<?> myProduct(@CurrentUser User currentUser) {
        return productService.myProductList(currentUser);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable("id") Long id) {
        return productService.getById(id);
    }

    @PutMapping("{id}/edit")
    public HttpEntity<?> editProductById(@PathVariable("id") Long id, ProductDto productDto, @CurrentUser User user) {
        return productService.editProduct(id, productDto, user);
    }

    @DeleteMapping("/{id}/delete")
    public HttpEntity<?> deleteProductById(@PathVariable("id") Long id, @CurrentUser User user) {
        return productService.deleteProduct(id, user);
    }


}
