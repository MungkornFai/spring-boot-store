package com.mungkorn.springbootecommerceapi.repositories;

import com.mungkorn.springbootecommerceapi.entities.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
