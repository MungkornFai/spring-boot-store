package com.mungkorn.springbootecommerceapi.repositories;

import com.mungkorn.springbootecommerceapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
