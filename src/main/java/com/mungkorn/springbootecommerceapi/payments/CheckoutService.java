package com.mungkorn.springbootecommerceapi.payments;

import com.mungkorn.springbootecommerceapi.orders.Order;
import com.mungkorn.springbootecommerceapi.carts.CartEmptyException;
import com.mungkorn.springbootecommerceapi.carts.CartNotFoundException;
import com.mungkorn.springbootecommerceapi.carts.CartRepository;
import com.mungkorn.springbootecommerceapi.orders.OrderRepository;

import com.mungkorn.springbootecommerceapi.auth.AuthService;
import com.mungkorn.springbootecommerceapi.carts.CartService;
import lombok.RequiredArgsConstructor;
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
