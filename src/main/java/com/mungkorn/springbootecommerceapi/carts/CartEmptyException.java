package com.mungkorn.springbootecommerceapi.carts;

public class CartEmptyException extends RuntimeException {
    public CartEmptyException() {
        super("Cart is empty");
    }
}
