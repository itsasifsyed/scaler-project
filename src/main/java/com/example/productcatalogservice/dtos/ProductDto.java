package com.example.productcatalogservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    Long id;
    String name;
    String description;
    String imageUrl;
    Double price;
    CategoryDto category;
}
