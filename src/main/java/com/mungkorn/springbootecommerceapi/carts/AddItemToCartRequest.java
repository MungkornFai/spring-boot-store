package com.mungkorn.springbootecommerceapi.carts;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemToCartRequest {
    @NotNull(message = "product is require")
    private Long productId;
}
