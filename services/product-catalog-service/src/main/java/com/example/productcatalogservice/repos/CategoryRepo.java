package com.example.productcatalogservice.repos;

import com.example.productcatalogservice.modals.Category;
import com.example.productcatalogservice.modals.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    Optional<Category> findById(Long id);
}
