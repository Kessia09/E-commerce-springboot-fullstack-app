package org.cat.irere.service;

import org.cat.irere.dto.AddToCartDTO;
import org.cat.irere.dto.CartItemDTO;

import java.util.List;

public interface CartService {
    CartItemDTO addToCart(Long customerId, AddToCartDTO addToCartDTO);

    CartItemDTO updateCartItem(Long customerId, Long cartItemId, Integer quantity);

    void removeFromCart(Long customerId, Long cartItemId);

    List<CartItemDTO> getCartItems(Long customerId);

    void clearCart(Long customerId);
}