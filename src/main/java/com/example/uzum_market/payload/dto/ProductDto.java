package com.example.uzum_market.payload.dto;

import com.example.uzum_market.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String productName;
    private String pathPhoto;
    private String price;
    private MultipartFile image;
    private String description;
    private Long id;
    private Category category;

}
