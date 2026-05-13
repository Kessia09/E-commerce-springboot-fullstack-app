package org.cat.irere.service;

import org.cat.irere.dto.AddToCartDTO;
import org.cat.irere.dto.PurchaseDTO;

import java.util.List;

public interface PurchaseService {
    PurchaseDTO purchaseItem(Long customerId, AddToCartDTO purchaseDTO);

    List<PurchaseDTO> getAllPurchases(Long customerId);

    List<PurchaseDTO> checkoutCart(Long customerId);
}