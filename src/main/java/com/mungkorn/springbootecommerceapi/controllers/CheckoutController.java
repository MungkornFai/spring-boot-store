package com.mungkorn.springbootecommerceapi.controllers;


import com.mungkorn.springbootecommerceapi.dtos.CheckoutRequest;
import com.mungkorn.springbootecommerceapi.dtos.CheckoutResponse;
import com.mungkorn.springbootecommerceapi.dtos.ErrorDto;
import com.mungkorn.springbootecommerceapi.entities.Order;
import com.mungkorn.springbootecommerceapi.entities.OrderItem;
import com.mungkorn.springbootecommerceapi.entities.OrderStatus;
import com.mungkorn.springbootecommerceapi.repositories.CartRepository;
import com.mungkorn.springbootecommerceapi.repositories.OrderRepository;
import com.mungkorn.springbootecommerceapi.services.AuthService;
import com.mungkorn.springbootecommerceapi.services.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/checkout")
public class CheckoutController {
    private final CartRepository cartRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final CartService cartService;


    @PostMapping
    public ResponseEntity<?> checkout(
            @Valid @RequestBody CheckoutRequest request
    ) {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if (cart == null) {
            return ResponseEntity.badRequest().body(
                    new ErrorDto("Cart not found")
            );
        }
        if(cart.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body(
                   new ErrorDto("Cart has no items")
            );
        }
        var order = new Order();
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus(OrderStatus.PENDING);
        order.setCustomer(authService.getCurrectUser());

        // loop item in the cart and get item
        cart.getItems().forEach(item -> {
            var orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setTotalPrice(item.getTotalPrice());
            orderItem.setUnitPrice(item.getProduct().getPrice());
            order.getItems().add(orderItem);
        });

        orderRepository.save(order);

        cartService.clearCart(cart.getId());
        return ResponseEntity.ok(new CheckoutResponse(order.getId()));
    }
}
