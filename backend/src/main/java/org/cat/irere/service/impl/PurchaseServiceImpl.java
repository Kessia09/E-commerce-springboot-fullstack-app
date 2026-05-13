package org.cat.irere.service.impl;

import org.cat.irere.dto.AddToCartDTO;
import org.cat.irere.dto.PurchaseDTO;
import org.cat.irere.dto.QuantityDTO;
import org.cat.irere.model.CartItem;
import org.cat.irere.model.Product;
import org.cat.irere.model.Purchase;
import org.cat.irere.repository.CartItemRepository;
import org.cat.irere.repository.CustomerRepository;
import org.cat.irere.repository.ProductRepository;
import org.cat.irere.repository.PurchaseRepository;
import org.cat.irere.service.CartService;
import org.cat.irere.service.PurchaseService;
import org.cat.irere.service.QuantityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final QuantityService quantityService;
    private final CartService cartService;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository,
            CartItemRepository cartItemRepository,
            QuantityService quantityService,
            CartService cartService) {
        this.purchaseRepository = purchaseRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.quantityService = quantityService;
        this.cartService = cartService;
    }

    @Override
    @Transactional
    public PurchaseDTO purchaseItem(Long customerId, AddToCartDTO purchaseDTO) {
        if (!customerRepository.existsById(customerId)) {
            throw new IllegalArgumentException("Customer not found");
        }

        Product product = productRepository.findById(purchaseDTO.getProductCode())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Integer availableQuantity = quantityService.getAvailableQuantity(purchaseDTO.getProductCode());
        if (availableQuantity < purchaseDTO.getQuantity()) {
            throw new IllegalArgumentException("Not enough quantity available");
        }

        BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(purchaseDTO.getQuantity()));

        Purchase purchase = new Purchase(
                customerId,
                purchaseDTO.getProductCode(),
                purchaseDTO.getQuantity(),
                total,
                LocalDate.now());

        Purchase savedPurchase = purchaseRepository.save(purchase);

        // Reduce the product quantity
        QuantityDTO quantityDTO = new QuantityDTO(
                purchaseDTO.getProductCode(),
                purchaseDTO.getQuantity(),
                "OUT");
        quantityService.removeQuantity(quantityDTO);

        savedPurchase.setProduct(product);
        return convertToDTO(savedPurchase);
    }

    @Override
    public List<PurchaseDTO> getAllPurchases(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new IllegalArgumentException("Customer not found");
        }

        return purchaseRepository.findByCustomerId(customerId).stream()
                .map(purchase -> {
                    Product product = productRepository.findById(purchase.getProductCode())
                            .orElseThrow(() -> new IllegalArgumentException("Product not found"));
                    purchase.setProduct(product);
                    return convertToDTO(purchase);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<PurchaseDTO> checkoutCart(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new IllegalArgumentException("Customer not found");
        }

        List<CartItem> cartItems = cartItemRepository.findByCustomerId(customerId);
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        // Verify if all products have enough quantity
        for (CartItem cartItem : cartItems) {
            Integer availableQuantity = quantityService.getAvailableQuantity(cartItem.getProductCode());
            if (availableQuantity < cartItem.getQuantity()) {
                throw new IllegalArgumentException(
                        "Not enough quantity available for product: " + cartItem.getProductCode());
            }
        }

        List<PurchaseDTO> purchaseDTOS = new ArrayList<>();

        // Process each cart item
        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductCode())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            Purchase purchase = new Purchase(
                    customerId,
                    cartItem.getProductCode(),
                    cartItem.getQuantity(),
                    total,
                    LocalDate.now());

            Purchase savedPurchase = purchaseRepository.save(purchase);

            // Reduce the product quantity
            QuantityDTO quantityDTO = new QuantityDTO(
                    cartItem.getProductCode(),
                    cartItem.getQuantity(),
                    "OUT");
            quantityService.removeQuantity(quantityDTO);

            savedPurchase.setProduct(product);
            purchaseDTOS.add(convertToDTO(savedPurchase));
        }

        // Clear the cart after successful checkout
        cartService.clearCart(customerId);

        return purchaseDTOS;
    }

    private PurchaseDTO convertToDTO(Purchase purchase) {
        return new PurchaseDTO(
                purchase.getId(),
                purchase.getProductCode(),
                purchase.getProduct().getName(),
                purchase.getQuantity(),
                purchase.getProduct().getPrice(),
                purchase.getTotal(),
                purchase.getDate());
    }
}