package com.mungkorn.springbootecommerceapi.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddItemToCartRequest {
    @NotBlank(message = "product is require")
    private Long productId;
}
