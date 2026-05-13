package org.cat.service;

import org.cat.dto.AddToCartDTO;
import org.cat.dto.PurchaseDTO;

import java.util.List;

public interface PurchaseService {
    PurchaseDTO purchaseItem(Long customerId, AddToCartDTO purchaseDTO);

    List<PurchaseDTO> getAllPurchases(Long customerId);

    List<PurchaseDTO> checkoutCart(Long customerId);
}
