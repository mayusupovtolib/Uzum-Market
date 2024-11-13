package com.example.uzum_market.entity;

import com.example.uzum_market.entity.base.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart extends AbsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;
    @ManyToOne
    private Product product;
    @Column(columnDefinition = "BIGINT DEFAULT 1")
    private Long count = 1L;


}
