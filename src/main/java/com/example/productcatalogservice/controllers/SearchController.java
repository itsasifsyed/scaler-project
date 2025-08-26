package com.example.productcatalogservice.controllers;

import com.example.productcatalogservice.dtos.SearchRequestDto;
import com.example.productcatalogservice.modals.Product;
import com.example.productcatalogservice.services.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {

    @Autowired
    private ISearchService searchService;

    @PostMapping("/search")
    public Page<Product> searchProducts(@RequestBody SearchRequestDto searchRequestDto) {
        return searchService.searchProducts(searchRequestDto.getQuery(),
                searchRequestDto.getPageNumber(),
                searchRequestDto.getPageSize(),searchRequestDto.getSortParams());
    }
}
