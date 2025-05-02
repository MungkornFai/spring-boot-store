package com.mungkorn.springbootecommerceapi.repositories;

import com.mungkorn.springbootecommerceapi.dtos.ProductDto;
import com.mungkorn.springbootecommerceapi.entities.Category;
import com.mungkorn.springbootecommerceapi.entities.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = "category")
    List<Product> findAllByCategoryId(Byte categoryId);

    @EntityGraph(attributePaths = "category")
    @Query("SELECT p from Product p")
    List<Product> findAllWithCategory();

}
