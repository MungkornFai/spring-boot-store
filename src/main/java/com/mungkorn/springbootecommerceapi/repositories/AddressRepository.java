package com.mungkorn.springbootecommerceapi.repositories;


import com.mungkorn.springbootecommerceapi.entities.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}
