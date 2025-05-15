package com.mungkorn.springbootecommerceapi.orders;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException() {
        super("Order not found");
    }
}
