package org.cat.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.cat.dto.AddToCartDTO;
import org.cat.dto.ApiResponse;
import org.cat.dto.CartItemDTO;
import org.cat.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller responsible for handling all shopping cart operations.
 * Provides endpoints for retrieving, adding, updating, removing, and clearing cart items.
 */
@RestController
@RequestMapping("/api/cart")
@Tag(name = "Cart", description = "Shopping cart management API")
public class CartController {

    // Service layer dependency to handle business logic
    private final CartService cartService;

    /**
     * Constructor-based dependency injection of CartService
     */
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * GET endpoint to retrieve all cart items for a specific customer
     * 
     * @param customerId ID of the customer
     * @return List of cart items wrapped in ApiResponse
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<List<CartItemDTO>>> getCartItems(@PathVariable Long customerId) {
        try {
            // Fetch cart items from service
            List<CartItemDTO> cartItems = cartService.getCartItems(customerId);
            return ResponseEntity.ok(ApiResponse.success("Cart items retrieved successfully", cartItems));
        
        } catch (IllegalArgumentException e) {
            // Handle invalid input (e.g., customer not found)
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        
        } catch (Exception e) {
            // Handle unexpected server errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while retrieving cart items"));
        }
    }

    /**
     * POST endpoint to add an item to the cart
     * 
     * @param customerId ID of the customer
     * @param addToCartDTO DTO containing product and quantity information
     * @return The added cart item
     */
    @PostMapping("/{customerId}/add")
    public ResponseEntity<ApiResponse<CartItemDTO>> addToCart(@PathVariable Long customerId,
            @RequestBody AddToCartDTO addToCartDTO) {
        try {
            // Add item to cart using service
            CartItemDTO cartItem = cartService.addToCart(customerId, addToCartDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Item added to cart successfully", cartItem));
        
        } catch (IllegalArgumentException e) {
            // Handle invalid request data
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while adding item to cart"));
        }
    }

    /**
     * PUT endpoint to update the quantity of a specific cart item
     * 
     * @param customerId ID of the customer
     * @param cartItemId ID of the cart item
     * @param quantity New quantity value
     * @return Updated cart item
     */
    @PutMapping("/{customerId}/update/{cartItemId}")
    public ResponseEntity<ApiResponse<CartItemDTO>> updateCartItem(@PathVariable Long customerId,
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity) {
        try {
            // Update cart item quantity
            CartItemDTO cartItem = cartService.updateCartItem(customerId, cartItemId, quantity);
            return ResponseEntity.ok(ApiResponse.success("Cart item updated successfully", cartItem));
        
        } catch (IllegalArgumentException e) {
            // Handle invalid parameters
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while updating cart item"));
        }
    }

    /**
     * DELETE endpoint to remove a specific item from the cart
     * 
     * @param customerId ID of the customer
     * @param cartItemId ID of the cart item
     * @return Success message
     */
    @DeleteMapping("/{customerId}/remove/{cartItemId}")
    public ResponseEntity<ApiResponse<Void>> removeFromCart(@PathVariable Long customerId,
            @PathVariable Long cartItemId) {
        try {
            // Remove item from cart
            cartService.removeFromCart(customerId, cartItemId);
            return ResponseEntity.ok(ApiResponse.success("Item removed from cart successfully"));
        
        } catch (IllegalArgumentException e) {
            // Handle invalid IDs
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while removing item from cart"));
        }
    }

    /**
     * DELETE endpoint to clear all items from a customer's cart
     * 
     * @param customerId ID of the customer
     * @return Success message
     */
    @DeleteMapping("/{customerId}/clear")
    public ResponseEntity<ApiResponse<Void>> clearCart(@PathVariable Long customerId) {
        try {
            // Clear entire cart
            cartService.clearCart(customerId);
            return ResponseEntity.ok(ApiResponse.success("Cart cleared successfully"));
        
        } catch (IllegalArgumentException e) {
            // Handle invalid customer ID
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while clearing cart"));
        }
    }
}
