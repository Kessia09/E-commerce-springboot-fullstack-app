package org.cat.irere.dto;

import java.time.LocalDate;

public class QuantityDTO {
    private Long id;
    private String productCode;
    private Integer quantity;
    private String operation;
    private LocalDate date;

    // Constructors
    public QuantityDTO() {
    }

    public QuantityDTO(String productCode, Integer quantity, String operation) {
        this.productCode = productCode;
        this.quantity = quantity;
        this.operation = operation;
        this.date = LocalDate.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}