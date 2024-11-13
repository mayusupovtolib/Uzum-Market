package com.example.uzum_market.controller;

import com.example.uzum_market.payload.dto.CategoryDto;
import com.example.uzum_market.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @PostMapping("/create-category")
    public HttpEntity<?> createCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.createCategory(categoryDto);
    }


    @GetMapping("/all-category")
    public HttpEntity<?> allCategory(@RequestParam(name = "parentId", required = false) Long parentId) {
        return categoryService.allCategory(parentId);
    }


}
