package com.mungkorn.springbootecommerceapi.repositories;


import com.mungkorn.springbootecommerceapi.entities.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
}
