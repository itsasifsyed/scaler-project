package com.example.productcatalogservice.modals;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Category extends BaseModal {
    private String name;
    private String description;
    private List<Product> products;
}
