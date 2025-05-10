package com.mungkorn.springbootecommerceapi.services;

import com.mungkorn.springbootecommerceapi.dtos.CheckoutRequest;
import com.mungkorn.springbootecommerceapi.dtos.CheckoutResponse;
import com.mungkorn.springbootecommerceapi.entities.Order;
import com.mungkorn.springbootecommerceapi.exceptions.CartEmptyException;
import com.mungkorn.springbootecommerceapi.exceptions.CartNotFoundException;
import com.mungkorn.springbootecommerceapi.repositories.CartRepository;
import com.mungkorn.springbootecommerceapi.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;

    public CheckoutResponse checkout(CheckoutRequest request) {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if(cart == null) {
            throw new CartNotFoundException();
        }

        if(cart.isEmpty()) {
            throw new CartEmptyException();
        }

        var order = Order.fromCart(cart, authService.getCurrectUser());

        orderRepository.save(order);

        cartService.clearCart(cart.getId());

        return new CheckoutResponse(order.getId());

    }
}
