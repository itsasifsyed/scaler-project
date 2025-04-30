package com.example.productcatalogservice.services;

import com.example.productcatalogservice.dtos.FakeStoreProductDto;
import com.example.productcatalogservice.modals.Category;
import com.example.productcatalogservice.modals.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class FakeStoreProductService implements IProductService {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Override
    public Product getProductById(Long id){
        RestTemplate restTemplate = restTemplateBuilder.build();
        String url = "http://fakestoreapi.com/products/" + id;
      ResponseEntity<FakeStoreProductDto> fakeStoreProductDto = restTemplate.getForEntity(url, FakeStoreProductDto.class, id);
      if(fakeStoreProductDto.getBody() != null && fakeStoreProductDto.getStatusCode().equals(HttpStatusCode.valueOf(200))) {
          return from(fakeStoreProductDto.getBody());
      }
       return null;
    }

    @Override
    public List<Product> getAllProducts() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        String url = "http://fakestoreapi.com/products/";
        ResponseEntity<FakeStoreProductDto[]> response = restTemplate.getForEntity(url, FakeStoreProductDto[].class);
        if(response.getBody() != null && response.getStatusCode() == HttpStatus.OK) {
            FakeStoreProductDto[] fakeStoreProducts = response.getBody();
            List<Product> products = new ArrayList<>();
            for(FakeStoreProductDto fakeStoreProductDto : fakeStoreProducts) {
                Product product = from(fakeStoreProductDto);
                products.add(product);
            }
            return products;
        }
        return null;
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        String url = "http://fakestoreapi.com/products/" + id;

        // make sure the product carries the right ID
        product.setId(id);

        // map domain → DTO
        FakeStoreProductDto dtoRequest = toDto(product);
        // perform the PUT
        ResponseEntity<FakeStoreProductDto> response = requestForEntity(
                url,
                HttpMethod.PUT,
                dtoRequest,
                FakeStoreProductDto.class,
                id
        );

        // on success, map DTO → domain
         if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
             FakeStoreProductDto output = response.getBody();
                return from(output);
         }
        // handle failure as you prefer
        return null;
    }

    @Override
    public Product createProduct(Product product) {
        return null;
    }

    @Override
    public Boolean deleteProduct(Long id) {
        return null;
    }

    public <T> ResponseEntity<T> requestForEntity(String url, HttpMethod httpMethod, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
        RestTemplate restTemplate =  restTemplateBuilder.build();
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables);
    }


    private Product from(FakeStoreProductDto fakeStoreProductDto) {
        Product product = new Product();
        product.setId(fakeStoreProductDto.getId());
        product.setName(fakeStoreProductDto.getTitle());
        product.setDescription(fakeStoreProductDto.getDescription());
        product.setPrice(fakeStoreProductDto.getPrice());
        product.setImageUrl(fakeStoreProductDto.getImage());
        Category category = new Category();
        category.setName(fakeStoreProductDto.getCategory());
        product.setCategory(category);
        return product;
    }

    private FakeStoreProductDto toDto(Product product) {
        FakeStoreProductDto dto = new FakeStoreProductDto();
        dto.setId(product.getId());
        dto.setTitle(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        // assuming Category.name holds exactly the API’s expected category string
        if (product.getCategory() != null) {
            dto.setCategory(product.getCategory().getName());
        } else {
            dto.setCategory(null);   // or set a default category string
        }
        dto.setImage(product.getImageUrl());
        return dto;
    }
}
