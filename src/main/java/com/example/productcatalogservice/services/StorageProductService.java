package com.example.productcatalogservice.services;

import com.example.productcatalogservice.modals.Product;
import com.example.productcatalogservice.repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("storage-product-service")
public class StorageProductService implements IProductService {
    @Autowired
    private ProductRepo productRepo;

    @Override
    public Product createProduct(Product product) {
        Optional<Product> productOptional = productRepo.findById(product.getId());
        return productOptional.orElseGet(() -> productRepo.save(product));
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products =   productRepo.findAll();
        return products;
    }

    @Override
    public Product getProductById(Long id) {
        Optional<Product> productOptional = productRepo.findById(id);
        return productOptional.orElse(null);
    }

    @Override
    public Boolean deleteProduct(Long id) {
        Optional<Product> productOptional = productRepo.findById(id);
        if(productOptional.isPresent()) {
            productRepo.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Product updateProduct(Long id, Product product){
        Optional<Product> productOptional = productRepo.findById(id);
        if(productOptional.isPresent()) {
           return productRepo.save(product);
        }
        return null;
    }
}
