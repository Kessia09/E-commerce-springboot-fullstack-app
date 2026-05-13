package org.cat.service;

import org.cat.dto.AddToCartDTO;
import org.cat.dto.CartItemDTO;

import java.util.List;

public interface CartService {
    CartItemDTO addToCart(Long customerId, AddToCartDTO addToCartDTO);

    CartItemDTO updateCartItem(Long customerId, Long cartItemId, Integer quantity);

    void removeFromCart(Long customerId, Long cartItemId);

    List<CartItemDTO> getCartItems(Long customerId);

    void clearCart(Long customerId);
}
