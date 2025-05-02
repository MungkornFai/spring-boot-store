package com.mungkorn.springbootecommerceapi.controllers;


import com.mungkorn.springbootecommerceapi.dtos.ProductDto;
import com.mungkorn.springbootecommerceapi.entities.Product;
import com.mungkorn.springbootecommerceapi.mappers.ProductMapper;
import com.mungkorn.springbootecommerceapi.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("products")
@AllArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @GetMapping
    public List<ProductDto> getProductRepository(@RequestParam(required = false, name = "categoryId") Byte categoryId) {
        List<Product> products;
        if (categoryId != null) {
           products = productRepository.findAllByCategoryId(categoryId);

        }else  {
            products = productRepository.findAllWithCategory();
        }
       return products.stream().map(productMapper::productToProductDto).toList();
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
        if(product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productMapper.productToProductDto(product));
    }
}
