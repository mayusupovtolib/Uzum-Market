package com.example.uzum_market.payload.projection;

public interface ProductProjection {

    Long getId();

    String getProductName();

    String getPathPhoto();

    String getPrice();

    String getDescription();

    Long getCategoryId();


}
