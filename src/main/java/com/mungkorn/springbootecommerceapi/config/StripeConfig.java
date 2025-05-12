package com.mungkorn.springbootecommerceapi.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {
    @Value("${stripe.secretKey}")
    private String secretKey;

    // tell spring to run this method when create this class

    @PostConstruct
    public void init(){
        Stripe.apiKey = secretKey;
    }
}
