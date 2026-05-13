package org.cat.service;

import org.cat.dto.QuantityDTO;

import java.util.List;

public interface QuantityService {
    List<QuantityDTO> getQuantitiesByProductCode(String productCode);

    QuantityDTO addQuantity(QuantityDTO quantityDTO);

    QuantityDTO removeQuantity(QuantityDTO quantityDTO);

    Integer getAvailableQuantity(String productCode);
}
