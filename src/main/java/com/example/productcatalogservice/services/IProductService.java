package com.example.productcatalogservice.services;

import com.example.productcatalogservice.modals.Product;

import java.util.List;

public interface IProductService {
    Product getProductById(Long id);
    List<Product> getAllProducts();
    Product updateProduct(Long id, Product product);
}
