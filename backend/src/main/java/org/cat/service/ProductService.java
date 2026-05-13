package org.cat.irere.service;

import org.cat.irere.dto.ProductDTO;
import org.cat.irere.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductDTO> getAllProducts();

    Optional<ProductDTO> getProductByCode(String code);

    ProductDTO saveProduct(Product product);

    void deleteProduct(String code);
}