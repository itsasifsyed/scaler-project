package com.example.productcatalogservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
    Long id;
    String name;
    String description;
    String imageUrl;
    Double price;
    CategoryDto category;
}
