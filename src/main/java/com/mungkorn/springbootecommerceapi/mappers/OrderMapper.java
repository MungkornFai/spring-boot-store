package com.mungkorn.springbootecommerceapi.mappers;

import com.mungkorn.springbootecommerceapi.dtos.OrderDto;
import com.mungkorn.springbootecommerceapi.entities.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
}
