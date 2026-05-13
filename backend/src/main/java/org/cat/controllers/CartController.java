package org.cat.irere.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.cat.irere.dto.AddToCartDTO;
import org.cat.irere.dto.ApiResponse;
import org.cat.irere.dto.CartItemDTO;
import org.cat.irere.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@Tag(name = "Cart", description = "Shopping cart management API")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<List<CartItemDTO>>> getCartItems(@PathVariable Long customerId) {
        try {
            List<CartItemDTO> cartItems = cartService.getCartItems(customerId);
            return ResponseEntity.ok(ApiResponse.success("Cart items retrieved successfully", cartItems));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while retrieving cart items"));
        }
    }

    @PostMapping("/{customerId}/add")
    public ResponseEntity<ApiResponse<CartItemDTO>> addToCart(@PathVariable Long customerId,
            @RequestBody AddToCartDTO addToCartDTO) {
        try {
            CartItemDTO cartItem = cartService.addToCart(customerId, addToCartDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Item added to cart successfully", cartItem));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while adding item to cart"));
        }
    }

    @PutMapping("/{customerId}/update/{cartItemId}")
    public ResponseEntity<ApiResponse<CartItemDTO>> updateCartItem(@PathVariable Long customerId,
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity) {
        try {
            CartItemDTO cartItem = cartService.updateCartItem(customerId, cartItemId, quantity);
            return ResponseEntity.ok(ApiResponse.success("Cart item updated successfully", cartItem));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while updating cart item"));
        }
    }

    @DeleteMapping("/{customerId}/remove/{cartItemId}")
    public ResponseEntity<ApiResponse<Void>> removeFromCart(@PathVariable Long customerId,
            @PathVariable Long cartItemId) {
        try {
            cartService.removeFromCart(customerId, cartItemId);
            return ResponseEntity.ok(ApiResponse.success("Item removed from cart successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while removing item from cart"));
        }
    }

    @DeleteMapping("/{customerId}/clear")
    public ResponseEntity<ApiResponse<Void>> clearCart(@PathVariable Long customerId) {
        try {
            cartService.clearCart(customerId);
            return ResponseEntity.ok(ApiResponse.success("Cart cleared successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while clearing cart"));
        }
    }
}