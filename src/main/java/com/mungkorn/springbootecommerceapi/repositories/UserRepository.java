package com.mungkorn.springbootecommerceapi.repositories;

import com.mungkorn.springbootecommerceapi.dtos.UserDto;
import com.mungkorn.springbootecommerceapi.entities.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);




    Optional<User> findByEmail(String email);
}
