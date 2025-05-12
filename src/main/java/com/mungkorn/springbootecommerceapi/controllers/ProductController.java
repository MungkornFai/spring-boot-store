package com.mungkorn.springbootecommerceapi.controllers;


import com.mungkorn.springbootecommerceapi.dtos.CreateProductRequest;
import com.mungkorn.springbootecommerceapi.dtos.ProductDto;
import com.mungkorn.springbootecommerceapi.entities.Product;
import com.mungkorn.springbootecommerceapi.mappers.ProductMapper;
import com.mungkorn.springbootecommerceapi.repositories.CategoryRepository;
import com.mungkorn.springbootecommerceapi.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("products")
@AllArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @GetMapping
    public List<ProductDto> getProductRepository(@RequestParam(required = false, name = "categoryId") Byte categoryId) {
        List<Product> products;
        if (categoryId != null) {
           products = productRepository.findAllByCategoryId(categoryId);

        }else  {
            products = productRepository.findAllWithCategory();
        }
       return products.stream().map(productMapper::toDto).toList();
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
        if(product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody ProductDto productDto,
            UriComponentsBuilder uriBuilder
            ) {
        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
          if(category == null) {
              return ResponseEntity.badRequest().build();
          }
        var product = productMapper.toEntity(productDto);
          product.setCategory(category);
        productRepository.save(product);

         productDto.setId(product.getId());

         var ui = uriBuilder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();
        return ResponseEntity.created(ui).body(productDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        var product = productRepository.findById(id).orElse(null);
        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if(product == null) {
            return ResponseEntity.notFound().build();
        }
        productMapper.updateProduct(productDto, product);
        product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());
        return ResponseEntity.ok(productDto);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
        if(product == null) {
            return ResponseEntity.notFound().build();
        }
        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }
}
