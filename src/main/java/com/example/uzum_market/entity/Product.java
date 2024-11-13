package com.example.uzum_market.entity;

import com.example.uzum_market.entity.base.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product extends AbsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private String pathPhoto;
    private String price;
    private String description;

    @ManyToOne
    private Category category;


}
