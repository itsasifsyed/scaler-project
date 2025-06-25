package com.example.productcatalogservice.modals;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Fetch;

import java.util.List;

@Getter
@Setter
@Entity
public class Category extends BaseModal {
    private String name;
    private String description;
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private List<Product> products;
}
