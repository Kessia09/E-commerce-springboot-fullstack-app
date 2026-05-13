package org.cat.irere.service.impl;

import org.cat.irere.dto.QuantityDTO;
import org.cat.irere.model.Quantity;
import org.cat.irere.repository.ProductRepository;
import org.cat.irere.repository.QuantityRepository;
import org.cat.irere.service.QuantityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuantityServiceImpl implements QuantityService {

    private final QuantityRepository quantityRepository;
    private final ProductRepository productRepository;

    public QuantityServiceImpl(QuantityRepository quantityRepository, ProductRepository productRepository) {
        this.quantityRepository = quantityRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<QuantityDTO> getQuantitiesByProductCode(String productCode) {
        return quantityRepository.findByProductCode(productCode).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public QuantityDTO addQuantity(QuantityDTO quantityDTO) {
        if (!productRepository.existsById(quantityDTO.getProductCode())) {
            throw new IllegalArgumentException("Product with code " + quantityDTO.getProductCode() + " does not exist");
        }

        Quantity quantity = new Quantity(
                quantityDTO.getProductCode(),
                quantityDTO.getQuantity(),
                "IN",
                LocalDate.now());

        Quantity savedQuantity = quantityRepository.save(quantity);
        return convertToDTO(savedQuantity);
    }

    @Override
    @Transactional
    public QuantityDTO removeQuantity(QuantityDTO quantityDTO) {
        if (!productRepository.existsById(quantityDTO.getProductCode())) {
            throw new IllegalArgumentException("Product with code " + quantityDTO.getProductCode() + " does not exist");
        }

        Integer availableQuantity = quantityRepository.getAvailableQuantity(quantityDTO.getProductCode());
        if (availableQuantity == null) {
            availableQuantity = 0;
        }

        if (availableQuantity < quantityDTO.getQuantity()) {
            throw new IllegalArgumentException("Not enough quantity available");
        }

        Quantity quantity = new Quantity(
                quantityDTO.getProductCode(),
                quantityDTO.getQuantity(),
                "OUT",
                LocalDate.now());

        Quantity savedQuantity = quantityRepository.save(quantity);
        return convertToDTO(savedQuantity);
    }

    @Override
    public Integer getAvailableQuantity(String productCode) {
        Integer availableQuantity = quantityRepository.getAvailableQuantity(productCode);
        return availableQuantity != null ? availableQuantity : 0;
    }

    private QuantityDTO convertToDTO(Quantity quantity) {
        QuantityDTO quantityDTO = new QuantityDTO(
                quantity.getProductCode(),
                quantity.getQuantity(),
                quantity.getOperation());
        quantityDTO.setId(quantity.getId());
        quantityDTO.setDate(quantity.getDate());
        return quantityDTO;
    }
}