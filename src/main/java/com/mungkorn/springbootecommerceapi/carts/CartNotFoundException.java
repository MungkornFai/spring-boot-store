package com.mungkorn.springbootecommerceapi.carts;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException() {
        super("Cart not found");
    }
}
