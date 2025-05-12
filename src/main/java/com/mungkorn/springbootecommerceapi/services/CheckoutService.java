package com.mungkorn.springbootecommerceapi.services;

import com.mungkorn.springbootecommerceapi.dtos.CheckoutRequest;
import com.mungkorn.springbootecommerceapi.dtos.CheckoutResponse;
import com.mungkorn.springbootecommerceapi.entities.Order;
import com.mungkorn.springbootecommerceapi.exceptions.CartEmptyException;
import com.mungkorn.springbootecommerceapi.exceptions.CartNotFoundException;
import com.mungkorn.springbootecommerceapi.repositories.CartRepository;
import com.mungkorn.springbootecommerceapi.repositories.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;

    @Value("${websiteUrl}")
    private String websiteUrl;

    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) throws StripeException {
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
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
                    .setCancelUrl(websiteUrl + "/checkout-cancel");

            order.getItems().forEach(item -> {
                var lineItem = SessionCreateParams.LineItem.builder()
                        .setQuantity(Long.valueOf(item.getQuantity()))
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("usd")
                                        .setUnitAmountDecimal(item.getUnitPrice().multiply(BigDecimal.valueOf(100)))
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName(item.getProduct().getName())
                                                        .build()
                                        )
                                        .build()

                        ).build();
                builder.addLineItem(lineItem);
            });

            var session = Session.create(builder.build());


            cartService.clearCart(cart.getId());

            return new CheckoutResponse(order.getId(),session.getUrl());
        } catch (StripeException e) {
            orderRepository.delete(order);
            throw e;
        }

    }
}
