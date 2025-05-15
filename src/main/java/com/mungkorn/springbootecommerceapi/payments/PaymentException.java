package com.mungkorn.springbootecommerceapi.payments;

public class PaymentException extends RuntimeException {

    public PaymentException(String message) {
        super(message);
    }
}
