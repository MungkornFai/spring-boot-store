package com.mungkorn.springbootecommerceapi.carts;

import com.mungkorn.springbootecommerceapi.products.ProductNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
@Tag(name = "Carts")
public class CartController {

    private final CartService cartService;


    @PostMapping
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriBuilder
    ){
        var cartDto = cartService.createCart();
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    @Operation(summary = "Adds a product to the cart.")
    public ResponseEntity<CartItemDto> addToCart(
            @Parameter(description = "The ID of the cart")
            @PathVariable(name = "cartId") UUID id,
            @Valid @RequestBody AddItemToCartRequest request
            ){
        var cartItemDto = cartService.addToCart(id,request.getProductId());

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }
    @GetMapping("/{cartId}")
    public CartDto getCart(@PathVariable UUID cartId) {
        return cartService.getCart(cartId);

    }

    @PutMapping("/{cartId}/items/{productId}")
    public CartItemDto updateCart(
            @PathVariable(name = "cartId") UUID cartId,
            @PathVariable(name = "productId") Long productId,
            @Valid @RequestBody UpdateCartItemRequest request

    ){
        return cartService.updateItem(cartId,productId,request.getQuantity());
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeItem(
            @PathVariable(name = "cartId") UUID cartId,
            @PathVariable(name = "productId") Long productId
    ) {
        cartService.removeItem(cartId,productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> clear(@PathVariable(name = "cartId") UUID cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleCartNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found"));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleProductNotFoundException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Product not found in the cart"));
    }
}
























