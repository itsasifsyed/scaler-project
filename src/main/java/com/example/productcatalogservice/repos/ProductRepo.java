package com.example.productcatalogservice.repos;

import com.example.productcatalogservice.modals.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Long> {
    Product save(Product product);

    Optional<Product> findById(Long id);

    List<Product> findAll();

    void deleteById(Long id);

    List<Product> findProductByOrderByPriceDesc();

    List<Product> findByNameEquals(String name, Pageable pageable);

    @Query("select p.name from Product p where p.id=?1")
    String findProductNameById(Long id);


    @Query("select c.name from Category  c join Product  p on p.category.id=c.id where p.id=:pid")
    String findCategoryNameFromProductId(Long pid);
}
