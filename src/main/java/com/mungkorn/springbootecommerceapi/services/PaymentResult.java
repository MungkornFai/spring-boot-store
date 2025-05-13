package com.mungkorn.springbootecommerceapi.services;

import com.mungkorn.springbootecommerceapi.entities.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaymentResult {
    private Long orderId;
    private PaymentStatus paymentStatus;
}
