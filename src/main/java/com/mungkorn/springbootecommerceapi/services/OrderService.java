package com.mungkorn.springbootecommerceapi.services;

import com.mungkorn.springbootecommerceapi.dtos.OrderDto;
import com.mungkorn.springbootecommerceapi.exceptions.OrderNotFoundException;
import com.mungkorn.springbootecommerceapi.mappers.OrderMapper;
import com.mungkorn.springbootecommerceapi.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderDto> getAllOrders() {
        var user = authService.getCurrectUser();
        var orders = orderRepository.getOrderByCustomer(user);
        return orders.stream().map(orderMapper::toDto).toList();
    }

    public OrderDto getOrder(Long orderId) {
        var order = orderRepository
                .getOrderWithItems(orderId)
                .orElseThrow(OrderNotFoundException::new);
        if (order == null) {
            throw new OrderNotFoundException();
        }
        var user = authService.getCurrectUser();

        if(!order.isPlacedBy(user)){
            throw new AccessDeniedException("Access denied");
        }
        return orderMapper.toDto(order);
    }

}
