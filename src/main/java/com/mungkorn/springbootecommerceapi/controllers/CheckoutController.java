package com.mungkorn.springbootecommerceapi.controllers;


import com.mungkorn.springbootecommerceapi.dtos.CheckoutRequest;
import com.mungkorn.springbootecommerceapi.dtos.CheckoutResponse;
import com.mungkorn.springbootecommerceapi.dtos.ErrorDto;
import com.mungkorn.springbootecommerceapi.exceptions.CartEmptyException;
import com.mungkorn.springbootecommerceapi.exceptions.CartNotFoundException;
import com.mungkorn.springbootecommerceapi.exceptions.PaymentException;
import com.mungkorn.springbootecommerceapi.repositories.OrderRepository;
import com.mungkorn.springbootecommerceapi.services.CheckoutService;
import com.mungkorn.springbootecommerceapi.services.WebhookRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;
    private final OrderRepository orderRepository;




    @PostMapping
    public CheckoutResponse checkout(@Valid @RequestBody CheckoutRequest request) {
        return checkoutService.checkout(request);
    }

    @PostMapping("/webhook")
    public void handleWebhook(
            @RequestHeader Map<String, String> headers,
            @RequestBody String payload
    ) {

        checkoutService.handleWebhookEvent(new WebhookRequest(headers, payload));
    }

    @ExceptionHandler({PaymentException.class})
    public ResponseEntity<?> handlePaymentException(PaymentException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("Error creating a checkout session"));
    }

    @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }

}












