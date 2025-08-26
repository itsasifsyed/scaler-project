package com.example.productcatalogservice.services;

import com.example.productcatalogservice.dtos.SearchRequestDto;
import com.example.productcatalogservice.dtos.SortParam;
import com.example.productcatalogservice.modals.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ISearchService {
    Page<Product> searchProducts(String query, Integer pageNumber, Integer pageSize, List<SortParam> sortParams);
}
