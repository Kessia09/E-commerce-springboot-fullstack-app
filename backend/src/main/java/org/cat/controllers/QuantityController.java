package org.cat.irere.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.cat.irere.dto.ApiResponse;
import org.cat.irere.dto.QuantityDTO;
import org.cat.irere.service.QuantityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quantities")
@Tag(name = "Quantity", description = "Product quantity management API")
public class QuantityController {

    private final QuantityService quantityService;

    public QuantityController(QuantityService quantityService) {
        this.quantityService = quantityService;
    }

    @GetMapping("/product/{productCode}")
    public ResponseEntity<ApiResponse<List<QuantityDTO>>> getQuantitiesByProductCode(@PathVariable String productCode) {
        try {
            List<QuantityDTO> quantities = quantityService.getQuantitiesByProductCode(productCode);
            return ResponseEntity.ok(ApiResponse.success("Quantities retrieved successfully", quantities));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while retrieving quantities"));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<QuantityDTO>> addQuantity(@RequestBody QuantityDTO quantityDTO) {
        try {
            QuantityDTO savedQuantity = quantityService.addQuantity(quantityDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Quantity added successfully", savedQuantity));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while adding quantity"));
        }
    }

    @PostMapping("/remove")
    public ResponseEntity<ApiResponse<QuantityDTO>> removeQuantity(@RequestBody QuantityDTO quantityDTO) {
        try {
            QuantityDTO removedQuantity = quantityService.removeQuantity(quantityDTO);
            return ResponseEntity.ok(ApiResponse.success("Quantity removed successfully", removedQuantity));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while removing quantity"));
        }
    }

    @GetMapping("/available/{productCode}")
    public ResponseEntity<ApiResponse<Integer>> getAvailableQuantity(@PathVariable String productCode) {
        try {
            Integer availableQuantity = quantityService.getAvailableQuantity(productCode);
            return ResponseEntity
                    .ok(ApiResponse.success("Available quantity retrieved successfully", availableQuantity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while retrieving available quantity"));
        }
    }
}