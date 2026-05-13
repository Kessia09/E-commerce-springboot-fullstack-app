package org.cat.irere.service.impl;

import org.cat.irere.dto.ProductDTO;
import org.cat.irere.model.Product;
import org.cat.irere.repository.ProductRepository;
import org.cat.irere.repository.QuantityRepository;
import org.cat.irere.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final QuantityRepository quantityRepository;

    public ProductServiceImpl(ProductRepository productRepository, QuantityRepository quantityRepository) {
        this.productRepository = productRepository;
        this.quantityRepository = quantityRepository;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductDTO> getProductByCode(String code) {
        return productRepository.findById(code)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional
    public ProductDTO saveProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(String code) {
        productRepository.deleteById(code);
    }

    private ProductDTO convertToDTO(Product product) {
        Integer availableQuantity = quantityRepository.getAvailableQuantity(product.getCode());
        if (availableQuantity == null) {
            availableQuantity = 0;
        }

        return new ProductDTO(
                product.getCode(),
                product.getName(),
                product.getProductType(),
                product.getPrice(),
                product.getInDate(),
                product.getImage(),
                availableQuantity);
    }
}