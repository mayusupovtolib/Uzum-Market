package com.example.uzum_market.service;

import com.example.uzum_market.entity.Category;
import com.example.uzum_market.payload.dto.CategoryDto;
import com.example.uzum_market.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;


    //  For admin
    public HttpEntity<?> createCategory(CategoryDto categoryDto) {
        try {
            Category category = new Category();
            category.setName(categoryDto.getName());
            category.setParentId(categoryDto.getParentId());
            categoryRepository.save(category);
            return ResponseEntity.status(201).body("Category is create: " + category);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e);
        }
    }


    public HttpEntity<?> allCategory(Long parentId) {
        try {
            List<Category> allByParentIdNull = categoryRepository.findAllByParentId(parentId);
            return ResponseEntity.status(200).body(allByParentIdNull);
        } catch (Exception e) {
            ResponseEntity.status(500).body(e);
        }
        return null;
    }

}
