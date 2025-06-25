package com.example.productcatalogservice.repos;

import com.example.productcatalogservice.modals.Product;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ProductRepoTest {
    @Autowired
    private ProductRepo productRepo;


    @Test
    @Transactional
    public void testQueries(){
        List<Product> productList = productRepo.findProductByOrderByPriceDesc();
        for(Product product:productList ){
            System.out.println(product.getName());
        }
    }
}