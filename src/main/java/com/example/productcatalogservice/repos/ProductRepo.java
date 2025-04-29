package com.example.productcatalogservice.repos;

import com.example.productcatalogservice.modals.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Long> {
    Product save(Product product);

    Optional<Product> findById(Long id);
}
