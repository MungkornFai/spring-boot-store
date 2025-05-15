package com.mungkorn.springbootecommerceapi.products;

import com.mungkorn.springbootecommerceapi.payments.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
