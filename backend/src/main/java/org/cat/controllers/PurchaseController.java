package org.cat.irere.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.cat.irere.dto.AddToCartDTO;
import org.cat.irere.dto.ApiResponse;
import org.cat.irere.dto.PurchaseDTO;
import org.cat.irere.service.PurchaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
@Tag(name = "Purchase", description = "Purchase management API")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<List<PurchaseDTO>>> getAllPurchases(@PathVariable Long customerId) {
        try {
            List<PurchaseDTO> purchases = purchaseService.getAllPurchases(customerId);
            return ResponseEntity.ok(ApiResponse.success("Purchases retrieved successfully", purchases));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while retrieving purchases"));
        }
    }

    @PostMapping("/{customerId}/buy")
    public ResponseEntity<ApiResponse<PurchaseDTO>> purchaseItem(@PathVariable Long customerId,
            @RequestBody AddToCartDTO purchaseDTO) {
        try {
            PurchaseDTO purchase = purchaseService.purchaseItem(customerId, purchaseDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Item purchased successfully", purchase));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while purchasing item"));
        }
    }

    @PostMapping("/{customerId}/checkout")
    public ResponseEntity<ApiResponse<List<PurchaseDTO>>> checkoutCart(@PathVariable Long customerId) {
        try {
            List<PurchaseDTO> purchases = purchaseService.checkoutCart(customerId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Cart checkout successful", purchases));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred during checkout"));
        }
    }
}