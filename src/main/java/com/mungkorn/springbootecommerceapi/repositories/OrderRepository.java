package com.mungkorn.springbootecommerceapi.repositories;

import com.mungkorn.springbootecommerceapi.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}