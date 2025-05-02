package com.mungkorn.springbootecommerceapi.mappers;

import com.mungkorn.springbootecommerceapi.dtos.ProductDto;
import com.mungkorn.springbootecommerceapi.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    ProductDto productToProductDto(Product product);
}
