package com.mungkorn.springbootecommerceapi.carts;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    // target = cartDto
    // source = cart entity
    // if target field and source field have same field name you can remove mapping
   // @Mapping(target = "items", source = "items")
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);
}
