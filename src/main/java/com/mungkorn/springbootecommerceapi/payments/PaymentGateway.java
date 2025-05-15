package com.mungkorn.springbootecommerceapi.payments;

import com.mungkorn.springbootecommerceapi.orders.Order;

import java.util.Optional;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
    Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);
}
