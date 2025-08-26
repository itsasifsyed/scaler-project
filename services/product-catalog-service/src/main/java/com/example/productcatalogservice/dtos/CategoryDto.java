package com.example.productcatalogservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryDto {
    Long id;
    String name;
    String description;
}