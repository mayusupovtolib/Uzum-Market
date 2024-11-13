package com.example.uzum_market.entity;

import com.example.uzum_market.entity.base.AbsEntity;

import javax.persistence.*;

@Entity
public class Test extends AbsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    private Test parentId;
}
