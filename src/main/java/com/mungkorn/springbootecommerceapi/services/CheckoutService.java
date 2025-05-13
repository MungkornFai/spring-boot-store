package com.mungkorn.springbootecommerceapi.services;

import com.mungkorn.springbootecommerceapi.dtos.CheckoutRequest;
import com.mungkorn.springbootecommerceapi.dtos.CheckoutResponse;
import com.mungkorn.springbootecommerceapi.entities.Order;
import com.mungkorn.springbootecommerceapi.entities.PaymentStatus;
import com.mungkorn.springbootecommerceapi.exceptions.CartEmptyException;
import com.mungkorn.springbootecommerceapi.exceptions.CartNotFoundException;
import com.mungkorn.springbootecommerceapi.exceptions.PaymentException;
import com.mungkorn.springbootecommerceapi.repositories.CartRepository;
import com.mungkorn.springbootecommerceapi.repositories.OrderRepository;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;
    private final StripePaymentGateway paymentGateway;



    @Transactional
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

        try{

            // Create a checkout session
            var session = paymentGateway.createCheckoutSession(order);

            cartService.clearCart(cart.getId());

            return new CheckoutResponse(order.getId(),session.getCheckoutUrl());
        } catch (PaymentException ex) {
            orderRepository.delete(order);
            throw ex;
        }

    }

    public void handleWebhookEvent(WebhookRequest request) {
            // WebhookRequest -> {orderId, paymentStatus}
        paymentGateway.parseWebhookRequest(request)
                .ifPresent(paymentResult -> {
                        var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
                        order.setStatus(paymentResult.getPaymentStatus());
                        orderRepository.save(order);
                });
    }
}
