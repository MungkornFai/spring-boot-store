package com.mungkorn.springbootecommerceapi.controllers;

import com.mungkorn.springbootecommerceapi.dtos.ErrorDto;
import com.mungkorn.springbootecommerceapi.dtos.OrderDto;

import com.mungkorn.springbootecommerceapi.exceptions.OrderNotFoundException;
import com.mungkorn.springbootecommerceapi.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;


    @GetMapping
    public List<OrderDto> getOrder() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public OrderDto getOrder(@PathVariable(name = "orderId") Long orderId) {
        return orderService.getOrder(orderId);
    }

    @ExceptionHandler({OrderNotFoundException.class})
    public ResponseEntity<Void> handleOrderNotFoundException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ErrorDto> handleAccessDeniedException(Exception ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ErrorDto(ex.getMessage())
        );
    }
}
