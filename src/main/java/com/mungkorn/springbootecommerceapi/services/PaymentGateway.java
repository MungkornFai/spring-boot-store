package com.mungkorn.springbootecommerceapi.services;

import com.mungkorn.springbootecommerceapi.entities.Order;

import java.util.Optional;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
    Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);
}
