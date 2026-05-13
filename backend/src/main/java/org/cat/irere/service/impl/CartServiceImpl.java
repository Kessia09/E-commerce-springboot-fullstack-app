package org.cat.irere.service.impl;

import org.cat.irere.dto.AddToCartDTO;
import org.cat.irere.dto.CartItemDTO;
import org.cat.irere.model.CartItem;
import org.cat.irere.model.Product;
import org.cat.irere.repository.CartItemRepository;
import org.cat.irere.repository.CustomerRepository;
import org.cat.irere.repository.ProductRepository;
import org.cat.irere.service.CartService;
import org.cat.irere.service.QuantityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final QuantityService quantityService;

    public CartServiceImpl(CartItemRepository cartItemRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository,
            QuantityService quantityService) {
        this.cartItemRepository = cartItemRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.quantityService = quantityService;
    }

    @Override
    @Transactional
    public CartItemDTO addToCart(Long customerId, AddToCartDTO addToCartDTO) {
        if (!customerRepository.existsById(customerId)) {
            throw new IllegalArgumentException("Customer not found");
        }

        Product product = productRepository.findById(addToCartDTO.getProductCode())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Integer availableQuantity = quantityService.getAvailableQuantity(addToCartDTO.getProductCode());
        if (availableQuantity < addToCartDTO.getQuantity()) {
            throw new IllegalArgumentException("Not enough quantity available");
        }

        Optional<CartItem> existingCartItem = cartItemRepository.findByCustomerIdAndProductCode(
                customerId, addToCartDTO.getProductCode());

        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            int newQuantity = cartItem.getQuantity() + addToCartDTO.getQuantity();

            if (availableQuantity < newQuantity) {
                throw new IllegalArgumentException("Not enough quantity available");
            }

            cartItem.setQuantity(newQuantity);
        } else {
            cartItem = new CartItem(customerId, addToCartDTO.getProductCode(), addToCartDTO.getQuantity());
        }

        CartItem savedCartItem = cartItemRepository.save(cartItem);
        savedCartItem.setProduct(product);
        return convertToDTO(savedCartItem);
    }

    @Override
    @Transactional
    public CartItemDTO updateCartItem(Long customerId, Long cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        if (!cartItem.getCustomerId().equals(customerId)) {
            throw new IllegalArgumentException("Cart item does not belong to the customer");
        }

        Integer availableQuantity = quantityService.getAvailableQuantity(cartItem.getProductCode());
        if (availableQuantity < quantity) {
            throw new IllegalArgumentException("Not enough quantity available");
        }

        cartItem.setQuantity(quantity);
        CartItem updatedCartItem = cartItemRepository.save(cartItem);

        Product product = productRepository.findById(updatedCartItem.getProductCode())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        updatedCartItem.setProduct(product);

        return convertToDTO(updatedCartItem);
    }

    @Override
    @Transactional
    public void removeFromCart(Long customerId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        if (!cartItem.getCustomerId().equals(customerId)) {
            throw new IllegalArgumentException("Cart item does not belong to the customer");
        }

        cartItemRepository.delete(cartItem);
    }

    @Override
    public List<CartItemDTO> getCartItems(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new IllegalArgumentException("Customer not found");
        }

        return cartItemRepository.findByCustomerId(customerId).stream()
                .map(cartItem -> {
                    Product product = productRepository.findById(cartItem.getProductCode())
                            .orElseThrow(() -> new IllegalArgumentException("Product not found"));
                    cartItem.setProduct(product);
                    return convertToDTO(cartItem);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void clearCart(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new IllegalArgumentException("Customer not found");
        }

        cartItemRepository.deleteByCustomerId(customerId);
    }

    private CartItemDTO convertToDTO(CartItem cartItem) {
        return new CartItemDTO(
                cartItem.getId(),
                cartItem.getProductCode(),
                cartItem.getProduct().getName(),
                cartItem.getQuantity(),
                cartItem.getProduct().getPrice(),
                cartItem.calculateTotal());
    }
}